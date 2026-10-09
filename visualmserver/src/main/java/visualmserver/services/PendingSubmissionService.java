package visualmserver.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import visualmserver.exceptions.ResourceNotFoundException;
import visualmserver.models.Material;
import visualmserver.models.PendingSubmission;
import visualmserver.models.SaveStatus;
import visualmserver.repositories.PendingSubmissionRepository;
import visualmserver.repositories.UserRepository;
import visualmserver.util.FileUploadHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Labels submitted by non-admins are parked in pending_submission (as JSON) until an admin approves them.
 * Only on approval do they become a row in the material table.
 */
@Service
public class PendingSubmissionService {

    @Autowired
    private PendingSubmissionRepository pendingSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private ObjectMapper objectMapper;

    public PendingSubmission submit(Material material) throws IOException {
        material.setSaveStatus(SaveStatus.PENDING_APPROVAL);

        PendingSubmission pending = new PendingSubmission();
        pending.setMaterialJson(objectMapper.writeValueAsString(material));
        pending.setSubmittedAt(LocalDateTime.now());
        return pendingSubmissionRepository.save(pending);
    }

    public List<PendingSubmission> findAll() {
        return pendingSubmissionRepository.findAll();
    }

    /**
     * The submitted label as a (not persisted) Material. Its creation date is the submission date.
     */
    public Material toMaterial(PendingSubmission submission) throws IOException {
        Material material = objectMapper.readValue(submission.getMaterialJson(), Material.class);
        if (material.getCreationDate() == null && submission.getSubmittedAt() != null) {
            material.setCreationDate(submission.getSubmittedAt().toLocalDate());
        }
        return material;
    }

    public Material getMaterial(Long id) throws IOException {
        return this.toMaterial(this.findById(id));
    }

    /**
     * Saves an admin's edits to a submission. It stays pending; submitter and submission date are kept.
     * Images: data: -> new upload, null/blank -> removed, anything else -> keep the stored image.
     */
    public Material update(Long id, Material edited) throws IOException {
        PendingSubmission pending = this.findById(id);
        Material stored = this.toMaterial(pending);

        materialService.validateSize(edited);

        edited.setSequenceNumber(null);
        edited.setSequenceNumberPublished(null);
        edited.setCreationDate(null);
        edited.setUser(stored.getUser());
        edited.setSaveStatus(SaveStatus.PENDING_APPROVAL);

        String imageFolder = stored.getUser() != null
                ? String.format("/images/material/%s/", stored.getUser().getId())
                : "/images/material/anonymous/";
        edited.setOverviewURL(this.resolveImage(edited.getOverviewURL(), stored.getOverviewURL(), imageFolder));
        edited.setCloseUpURL(this.resolveImage(edited.getCloseUpURL(), stored.getCloseUpURL(), imageFolder));

        pending.setMaterialJson(objectMapper.writeValueAsString(edited));
        pendingSubmissionRepository.save(pending);

        return this.toMaterial(pending);
    }

    private String resolveImage(String image, String storedImage, String imageFolder) throws IOException {
        if (image == null || image.isBlank()) {
            return null;
        }
        if (image.startsWith("data:")) {
            return FileUploadHandler.upload(image, imageFolder);
        }
        // Never trust a client-supplied path; keep what is already stored
        return storedImage;
    }

    public Material approve(Long id) throws IOException {
        PendingSubmission pending = this.findById(id);
        Material material = objectMapper.readValue(pending.getMaterialJson(), Material.class);

        // Re-fetch managed User entity to avoid detached entity errors.
        if (material.getUser() != null && material.getUser().getId() > 0) {
            material.setUser(userRepository.getUserById(material.getUser().getId()));
        }

        material.setSequenceNumber(null);
        material.setSequenceNumberPublished(materialService.nextPublishedSequenceNumber());
        material.setSaveStatus(SaveStatus.PUBLISHED);

        Material savedMaterial = materialService.insertMaterial(material, null);
        pendingSubmissionRepository.deleteById(id);

        return savedMaterial;
    }

    public void reject(Long id) {
        this.findById(id);
        pendingSubmissionRepository.deleteById(id);
    }

    private PendingSubmission findById(Long id) {
        return pendingSubmissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pending submission not found with id=%d", id)));
    }
}
