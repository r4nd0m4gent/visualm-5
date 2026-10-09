package visualmserver.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import visualmserver.exceptions.AuthorizationException;
import visualmserver.exceptions.InvalidDataException;
import visualmserver.exceptions.PreConditionException;
import visualmserver.exceptions.ResourceNotFoundException;
import visualmserver.models.Material;
import visualmserver.models.PendingSubmission;
import visualmserver.models.SaveStatus;
import visualmserver.models.User;
import visualmserver.repositories.MaterialsRepository;
import visualmserver.repositories.UserRepository;
import visualmserver.services.MaterialService;
import visualmserver.services.PendingSubmissionService;
import visualmserver.services.SubmissionNotifier;
import visualmserver.util.FileUploadHandler;
import visualmserver.util.JWTokenInfo;

import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/materials")
public class MaterialsController {
    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private PendingSubmissionService pendingSubmissionService;

    @Autowired
    private SubmissionNotifier submissionNotifier;

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
            Material savedMaterial = materialService.insertMaterial(material, null);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{sequenceNumber}").buildAndExpand(savedMaterial.getSequenceNumber()).toUri();
            return ResponseEntity.created(uri).body(savedMaterial);
        }

        // Non-admin (or admin saving as draft/pending): store as pending submission.
        pendingSubmissionService.submit(material);
        submissionNotifier.notifyAdmins(material);

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
            submissionNotifier.notifyAdmins(material);
        }

        String imageFolder = material.getUser() != null
                ? String.format("/images/material/%s/", material.getUser().getId())
                : "/images/material/anonymous/";

        // data: -> new upload, null/blank -> image deleted, anything else -> keep the stored image
        material.setOverviewURL(this.resolveUpdatedImage(material.getOverviewURL(), foundMaterial.getOverviewURL(), imageFolder));
        material.setCloseUpURL(this.resolveUpdatedImage(material.getCloseUpURL(), foundMaterial.getCloseUpURL(), imageFolder));

        Material savedMaterial = materialService.insertMaterial(material, foundMaterial);
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
        List<Material> materials = new ArrayList<>();
        for (PendingSubmission sub : pendingSubmissionService.findAll()) {
            Material m = pendingSubmissionService.toMaterial(sub);
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

        Material savedMaterial = pendingSubmissionService.approve(sequenceNumber);

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

        pendingSubmissionService.reject(sequenceNumber);

        return ResponseEntity.noContent().build();
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
        materialService.validateSize(material);
    }
}
