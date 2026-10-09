package visualmserver.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import visualmserver.exceptions.AuthorizationException;
import visualmserver.exceptions.InvalidDataException;
import visualmserver.exceptions.PreConditionException;
import visualmserver.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import visualmserver.models.Ingredient;
import visualmserver.models.Material;
import visualmserver.models.MaterialIngredient;
import visualmserver.models.PendingSubmission;
import visualmserver.models.SaveStatus;
import visualmserver.models.User;
import visualmserver.repositories.IngredientRepository;
import visualmserver.repositories.MaterialIngredientRepository;
import visualmserver.repositories.MaterialsRepository;
import visualmserver.repositories.PendingSubmissionRepository;
import visualmserver.repositories.UserRepository;
import visualmserver.services.EmailSenderService;
import visualmserver.util.FileUploadHandler;
import visualmserver.util.JWTokenInfo;

import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/materials")
public class MaterialsController {
    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MaterialIngredientRepository materialIngredientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PendingSubmissionRepository pendingSubmissionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${frontend.url:}")
    private String frontendUrl;

    @GetMapping
    public List<Material> getAllMaterials() {
        return this.materialsRepository.findAll();
    }

    @GetMapping("/{sequenceNumber}")
    public Material getMaterialFromSequenceNumber(@PathVariable Long sequenceNumber) throws IOException {
        Material material = this.materialsRepository.getMaterialBySequenceNumber(sequenceNumber);

        if (material == null) {
            throw new ResourceNotFoundException(String.format("Material not found with sequenceNumber=%d", sequenceNumber));
        }

        return material;
    }

    @GetMapping("published/{sequenceNumberPublished}")
    public Material getMaterialFromSequenceNumberPublished(@PathVariable Long sequenceNumberPublished) throws IOException {
        Material material = this.materialsRepository.getMaterialBySequenceNumberPublished(sequenceNumberPublished);

        if (material == null) {
            throw new ResourceNotFoundException(String.format("Material not found with sequenceNumberPublished=%d", sequenceNumberPublished));
        }

        return material;
    }

    @GetMapping("/user/{id}")
    public List<Material> getMaterialFromUserId(@PathVariable int id) throws IOException {
        User user = this.userRepository.getUserById(id);
        List<Material> materials = this.materialsRepository.getMaterialsByUser(user);

        return materials;
    }

    @DeleteMapping("/{sequenceNumber}")
    public ResponseEntity<Long> deleteMaterial(@PathVariable Long sequenceNumber, @RequestAttribute(value = JWTokenInfo.ATTRIBUTE_KEY) JWTokenInfo tokenInfo) {
        if (!tokenInfo.isAdmin()) {
            throw new AuthorizationException("Only administrators are able to remove existing users.");
        }

        if (this.materialsRepository.getMaterialBySequenceNumber(sequenceNumber) == null) {
            throw new ResourceNotFoundException(String.format("Material not found with sequenceNumber=%d", sequenceNumber));
        }

        Long deletedMaterialCount = this.materialsRepository.deleteMaterialBySequenceNumber(sequenceNumber);

        return ResponseEntity.ok().body(deletedMaterialCount);
    }

    @PostMapping
    public ResponseEntity<Material> saveMaterial(@RequestBody @Valid Material material, Errors errors,
            @RequestAttribute(value = JWTokenInfo.ATTRIBUTE_KEY) JWTokenInfo tokenInfo) throws IOException {
        if (errors.hasErrors()) {
            throw new InvalidDataException(errors.toString());
        }

        this.validateSize(material);

        // Force INSERT: set sequenceNumber to null so JPA auto-generates.
        material.setSequenceNumber(null);

        // Resolve submitter.
        User submitter = null;
        if (material.getUser() != null && material.getUser().getId() > 0) {
            submitter = userRepository.getUserById(material.getUser().getId());
        }
        if (submitter == null && tokenInfo.getId() > 0) {
            submitter = userRepository.getUserById((int) tokenInfo.getId());
        }
        // Anonymous submission — user_id is nullable; no fallback to users[0]
        material.setUser(submitter);

        // Non-admin users cannot publish directly — route through approval
        if (material.getSaveStatus() == SaveStatus.PUBLISHED && !tokenInfo.isAdmin()) {
            material.setSaveStatus(SaveStatus.PENDING_APPROVAL);
        }

        String imageFolder = submitter != null
                ? String.format("/images/material/%s/", submitter.getId())
                : "/images/material/anonymous/";

        // Duplicated labels send the parent's stored image path instead of base64 data; reuse that file as-is
        material.setOverviewURL(this.resolveImage(material.getOverviewURL(), imageFolder));
        material.setCloseUpURL(this.resolveImage(material.getCloseUpURL(), imageFolder));

        // Admin direct publish — insert immediately.
        if (material.getSaveStatus() == SaveStatus.PUBLISHED && tokenInfo.isAdmin()) {
            Material savedMaterial = this.insertMaterial(material, null);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{sequenceNumber}").buildAndExpand(savedMaterial.getSequenceNumber()).toUri();
            return ResponseEntity.created(uri).body(savedMaterial);
        }

        // Non-admin (or admin saving as draft/pending): store as pending submission.
        material.setSaveStatus(SaveStatus.PENDING_APPROVAL);

        PendingSubmission pending = new PendingSubmission();
        pending.setMaterialJson(objectMapper.writeValueAsString(material));
        pending.setSubmittedAt(LocalDateTime.now());
        pendingSubmissionRepository.save(pending);

        notifyAdminsOnSubmission(material);

        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{sequenceNumber}")
    public ResponseEntity<Material> updateMaterial(@PathVariable long sequenceNumber, @RequestBody @Valid Material material, Errors errors,
            @RequestAttribute(value = JWTokenInfo.ATTRIBUTE_KEY) JWTokenInfo tokenInfo) throws IOException {
        if (errors.hasErrors()) {
            throw new InvalidDataException("Invalid user object data.");
        }

        this.validateSize(material);

        if (material.getSequenceNumber() != sequenceNumber) {
            throw new PreConditionException(String.format("Material-sequenceNumber=%d does not match with parameter=%d",
                    material.getSequenceNumber(), sequenceNumber));
        }

        Material foundMaterial = materialsRepository.getMaterialBySequenceNumber(sequenceNumber);

        if (foundMaterial == null) {
            throw new ResourceNotFoundException(String.format("Material not found with sequenceNumber=%d", sequenceNumber));
        }

        // Preserve the original submitter from the existing material
        material.setUser(foundMaterial.getUser());

        // Non-admin users cannot publish directly — route through approval
        if (material.getSaveStatus() == SaveStatus.PUBLISHED && !tokenInfo.isAdmin()) {
            material.setSaveStatus(SaveStatus.PENDING_APPROVAL);
        }

        boolean wasNotPending = foundMaterial.getSaveStatus() != SaveStatus.PENDING_APPROVAL;
        if (material.getSaveStatus() == SaveStatus.PENDING_APPROVAL && wasNotPending) {
            notifyAdminsOnSubmission(material);
        }

        String imageFolder = material.getUser() != null
                ? String.format("/images/material/%s/", material.getUser().getId())
                : "/images/material/anonymous/";

        // data: -> new upload, null/blank -> image deleted, anything else -> keep the stored image
        material.setOverviewURL(this.resolveUpdatedImage(material.getOverviewURL(), foundMaterial.getOverviewURL(), imageFolder));
        material.setCloseUpURL(this.resolveUpdatedImage(material.getCloseUpURL(), foundMaterial.getCloseUpURL(), imageFolder));

        Material savedMaterial = this.insertMaterial(material, foundMaterial);
        return ResponseEntity.ok(savedMaterial);
    }

    @GetMapping("/published")
    public List<Material> getPublishedMaterials() {
        return this.materialsRepository.getMaterialsBySaveStatus(SaveStatus.PUBLISHED);
    }

    @GetMapping("/pending")
    public List<Material> getPendingMaterials(@RequestAttribute(value = JWTokenInfo.ATTRIBUTE_KEY) JWTokenInfo tokenInfo) throws IOException {
        if (!tokenInfo.isAdmin()) {
            throw new AuthorizationException("Only administrators can view pending materials.");
        }
        List<PendingSubmission> submissions = pendingSubmissionRepository.findAll();
        List<Material> materials = new ArrayList<>();
        for (PendingSubmission sub : submissions) {
            Material m = objectMapper.readValue(sub.getMaterialJson(), Material.class);
            m.setSequenceNumber(sub.getId());
            m.setOverviewURL(FileUploadHandler.getFileBase64(m.getOverviewURL()));
            m.setCloseUpURL(FileUploadHandler.getFileBase64(m.getCloseUpURL()));
            materials.add(m);
        }
        return materials;
    }

    @PutMapping("/approve/{sequenceNumber}")
    public ResponseEntity<Material> approveMaterial(@PathVariable Long sequenceNumber,
            @RequestAttribute(value = JWTokenInfo.ATTRIBUTE_KEY) JWTokenInfo tokenInfo) throws IOException {
        if (!tokenInfo.isAdmin()) {
            throw new AuthorizationException("Only administrators can approve materials.");
        }

        PendingSubmission pending = pendingSubmissionRepository.findById(sequenceNumber)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pending submission not found with id=%d", sequenceNumber)));

        Material material = objectMapper.readValue(pending.getMaterialJson(), Material.class);

        // Re-fetch managed User entity to avoid detached entity errors.
        if (material.getUser() != null && material.getUser().getId() > 0) {
            material.setUser(userRepository.getUserById(material.getUser().getId()));
        }

        material.setSequenceNumber(null);

        // Assign a published sequence number.
        List<Material> publishedMaterials = this.materialsRepository.getMaterialsBySaveStatus(SaveStatus.PUBLISHED);
        long maxPublished = publishedMaterials.stream()
                .filter(m -> m.getSequenceNumberPublished() != null)
                .mapToLong(Material::getSequenceNumberPublished)
                .max()
                .orElse(0);
        material.setSequenceNumberPublished(maxPublished + 1);
        material.setSaveStatus(SaveStatus.PUBLISHED);

        Material savedMaterial = this.insertMaterial(material, null);
        pendingSubmissionRepository.deleteById(sequenceNumber);

        savedMaterial.setOverviewURL(FileUploadHandler.getFileBase64(savedMaterial.getOverviewURL()));
        savedMaterial.setCloseUpURL(FileUploadHandler.getFileBase64(savedMaterial.getCloseUpURL()));

        return ResponseEntity.ok().body(savedMaterial);
    }

    @PutMapping("/reject/{sequenceNumber}")
    public ResponseEntity<Void> rejectMaterial(@PathVariable Long sequenceNumber,
            @RequestAttribute(value = JWTokenInfo.ATTRIBUTE_KEY) JWTokenInfo tokenInfo) {
        if (!tokenInfo.isAdmin()) {
            throw new AuthorizationException("Only administrators can reject materials.");
        }

        pendingSubmissionRepository.findById(sequenceNumber)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pending submission not found with id=%d", sequenceNumber)));

        pendingSubmissionRepository.deleteById(sequenceNumber);

        return ResponseEntity.noContent().build();
    }

    private void notifyAdminsOnSubmission(Material material) {
        if (material.getUser() == null || material.getUser().getOrganisation() == null) return;
        String organisation = material.getUser().getOrganisation();
        List<User> admins = userRepository.findByOrganisationAndAdmin(organisation, true);
        String base = (frontendUrl != null && !frontendUrl.isBlank())
                ? (frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl)
                : "";
        String adminPanelLink = base + "/#/admin";
        String body = "<p>A new label has been submitted for review under your organisation <strong>" + organisation + "</strong>.</p>"
                + "<p><a href='" + adminPanelLink + "'>Go to admin panel</a></p>";
        for (User admin : admins) {
            if (admin.isNotifyOnSubmission()) {
                emailSenderService.sendMail("Visualm5", admin.getEmail(), "Label submitted", body);
            }
        }
    }

    private Material insertMaterial(Material material, Material existingMaterial) {
        // Temporarily clear materialIngredients before saving Material to avoid
        // Hibernate attempting to persist stale composite-key join rows.
        Set<MaterialIngredient> ingredients = material.getMaterialIngredients() != null
                ? new HashSet<>(material.getMaterialIngredients()) : new HashSet<>();
        material.setMaterialIngredients(new HashSet<>());

        Material savedMaterial = this.materialsRepository.save(material);

        if (existingMaterial != null) {
            this.materialIngredientRepository.deleteMaterialIngredientBySequenceNumber(existingMaterial.getSequenceNumber());
        }

        for (MaterialIngredient materialIngredient : ingredients) {
            Ingredient ingredient = materialIngredient.getIngredient();
            if (ingredient.getId() == 0) {
                Optional<Ingredient> existing = ingredientRepository.findByNameAndType(ingredient.getName(), ingredient.getType());
                if (existing.isPresent()) {
                    ingredient.setId(existing.get().getId());
                } else {
                    int newId = ingredientRepository.findMaxId() + 1;
                    ingredientRepository.insertIngredientWithId(newId, ingredient.getName(), ingredient.getType());
                    ingredient.setId(newId);
                }
            }
            this.materialIngredientRepository.insertMaterialIngredient(savedMaterial.getSequenceNumber(),
                    ingredient.getId(), materialIngredient.getAmount());
        }
        savedMaterial.setMaterialIngredients(ingredients);

        return savedMaterial;
    }

    private String resolveImage(String image, String imageFolder) throws IOException {
        if (image == null || image.isBlank()) {
            return null;
        }

        if (image.startsWith("data:")) {
            return FileUploadHandler.upload(image, imageFolder);
        }

        // Only accept paths that point to images previously stored by this server
        if (image.startsWith("/images/material/") && !image.contains("..")) {
            return image;
        }

        return null;
    }

    private String resolveUpdatedImage(String image, String storedImage, String imageFolder) throws IOException {
        if (image == null || image.isBlank()) {
            return null;
        }

        if (image.startsWith("data:")) {
            return FileUploadHandler.upload(image, imageFolder);
        }

        // Never trust a client-supplied path on update; keep what is already stored
        return storedImage;
    }

    private void validateSize(Material material) {
        System.out.println(material.getSteps().split("\\|").length);
        if (material.getSteps().split("\\|").length > Material.MAXIMUM_STEPS) {
            throw new InvalidDataException("Material can only contain a maximum of 8 steps.");
        }

        if (material.getMaterialIngredients().size() > Material.MAXIMUM_INGREDIENTS ) {
            throw new InvalidDataException("Material can only contain a maximum of 6 ingredients.");
        }
    }
}
