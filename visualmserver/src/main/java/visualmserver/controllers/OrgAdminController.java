package visualmserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import visualmserver.exceptions.PreConditionException;
import visualmserver.exceptions.ResourceNotFoundException;
import visualmserver.models.LabelTemplate;
import visualmserver.models.Material;
import visualmserver.models.OrgAdmin;
import visualmserver.models.Report;
import visualmserver.models.SaveStatus;
import visualmserver.models.User;
import visualmserver.repositories.LabelTemplateRepository;
import visualmserver.repositories.MaterialsRepository;
import visualmserver.repositories.OrgAdminRepository;
import visualmserver.repositories.ReportRepository;
import visualmserver.repositories.UserRepository;
import visualmserver.util.FileUploadHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/org-admin")
public class OrgAdminController {

    @Autowired
    private OrgAdminRepository orgAdminRepository;

    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private LabelTemplateRepository labelTemplateRepository;

    @Value("${frontend.url:}")
    private String frontendUrl;

    /**
     * Public: list all organisations (name + logo only) for the label form dropdown.
     */
    @GetMapping("/organisations")
    public List<Map<String, String>> getOrganisations() {
        return orgAdminRepository.findAll().stream()
                .map(admin -> {
                    Map<String, String> org = new HashMap<>();
                    org.put("organisation", admin.getOrganisation());
                    org.put("logoPath", admin.getLogoPath());
                    return org;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Browser entry point: redirect to the Angular hash-based admin panel.
     * This is the URL stored in the DB and shared with org admins.
     */
    @GetMapping("/access/{token}")
    public ResponseEntity<Void> redirectToPanel(@PathVariable String token) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        String baseUrl = frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
        String redirectUrl = baseUrl + "/#/org-admin/access/" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * API: Validate a private access token and return the org admin details.
     */
    @GetMapping("/api/{token}")
    public ResponseEntity<OrgAdmin> accessPanel(@PathVariable String token) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(admin);
    }

    /**
     * API: Update organisation details (name, logo) via private token.
     */
    @PutMapping("/api/{token}")
    public ResponseEntity<OrgAdmin> updateOrg(@PathVariable String token, @RequestBody OrgAdmin updates) throws IOException {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        if (updates.getOrganisation() != null) {
            admin.setOrganisation(updates.getOrganisation());
        }
        if (updates.getLogoPath() != null) {
            String logoData = updates.getLogoPath();
            if (logoData.startsWith("data:")) {
                String savedPath = FileUploadHandler.upload(logoData, String.format("/images/logo/%d/", admin.getId()));
                admin.setLogoPath(savedPath);
            } else {
                admin.setLogoPath(logoData);
            }
        }
        orgAdminRepository.save(admin);
        return ResponseEntity.ok(admin);
    }

    /**
     * API: Get pending materials via org-admin token (no JWT required).
     */
    @GetMapping("/api/{token}/pending")
    public ResponseEntity<List<Material>> getPendingMaterials(@PathVariable String token) throws IOException {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        List<Material> materials = materialsRepository.getMaterialsByOrganisationAndSaveStatus(
                admin.getOrganisation(), SaveStatus.PENDING_APPROVAL);
        for (Material material : materials) {
            material.setOverviewURL(FileUploadHandler.getFileBase64(material.getOverviewURL()));
            material.setCloseUpURL(FileUploadHandler.getFileBase64(material.getCloseUpURL()));
        }
        return ResponseEntity.ok(materials);
    }

    /**
     * API: Approve a pending material via org-admin token (no JWT required).
     */
    @PutMapping("/api/{token}/approve/{sequenceNumber}")
    public ResponseEntity<Material> approveMaterial(@PathVariable String token,
                                                     @PathVariable Long sequenceNumber) throws IOException {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        Material material = materialsRepository.getMaterialBySequenceNumber(sequenceNumber);
        if (material == null) {
            throw new ResourceNotFoundException(String.format("Material not found with sequenceNumber=%d", sequenceNumber));
        }
        if (material.getSaveStatus() != SaveStatus.PENDING_APPROVAL) {
            throw new PreConditionException("Material is not pending approval.");
        }

        List<Material> publishedMaterials = materialsRepository.getMaterialsBySaveStatus(SaveStatus.PUBLISHED);
        long maxPublished = publishedMaterials.stream()
                .filter(m -> m.getSequenceNumberPublished() != null)
                .mapToLong(Material::getSequenceNumberPublished)
                .max()
                .orElse(0);
        material.setSequenceNumberPublished(maxPublished + 1);
        material.setSaveStatus(SaveStatus.PUBLISHED);

        Material savedMaterial = materialsRepository.save(material);
        savedMaterial.setOverviewURL(FileUploadHandler.getFileBase64(savedMaterial.getOverviewURL()));
        savedMaterial.setCloseUpURL(FileUploadHandler.getFileBase64(savedMaterial.getCloseUpURL()));
        return ResponseEntity.ok(savedMaterial);
    }

    /**
     * API: Reject a pending material via org-admin token (no JWT required).
     */
    @PutMapping("/api/{token}/reject/{sequenceNumber}")
    public ResponseEntity<Material> rejectMaterial(@PathVariable String token,
                                                    @PathVariable Long sequenceNumber) throws IOException {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        Material material = materialsRepository.getMaterialBySequenceNumber(sequenceNumber);
        if (material == null) {
            throw new ResourceNotFoundException(String.format("Material not found with sequenceNumber=%d", sequenceNumber));
        }

        material.setSaveStatus(SaveStatus.DRAFT);
        Material savedMaterial = materialsRepository.save(material);
        savedMaterial.setOverviewURL(FileUploadHandler.getFileBase64(savedMaterial.getOverviewURL()));
        savedMaterial.setCloseUpURL(FileUploadHandler.getFileBase64(savedMaterial.getCloseUpURL()));
        return ResponseEntity.ok(savedMaterial);
    }

    /**
     * API: Get all materials via org-admin token.
     */
    @GetMapping("/api/{token}/materials")
    public ResponseEntity<List<Material>> getAllMaterials(@PathVariable String token) throws IOException {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        List<Material> materials = materialsRepository.getMaterialsByOrganisation(admin.getOrganisation());
        for (Material material : materials) {
            material.setOverviewURL(FileUploadHandler.getFileBase64(material.getOverviewURL()));
            material.setCloseUpURL(FileUploadHandler.getFileBase64(material.getCloseUpURL()));
        }
        return ResponseEntity.ok(materials);
    }

    /**
     * API: Get all users via org-admin token.
     */
    @GetMapping("/api/{token}/users")
    public ResponseEntity<List<User>> getAllUsers(@PathVariable String token) throws IOException {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setMediaURL(FileUploadHandler.getFileBase64(user.getimg_path()));
        }
        return ResponseEntity.ok(users);
    }

    /**
     * API: Get all reports via org-admin token.
     */
    @GetMapping("/api/{token}/reports")
    public ResponseEntity<List<Report>> getAllReports(@PathVariable String token) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(reportRepository.findAll());
    }

    /**
     * API: Mark a report as solved/not solved via org-admin token.
     */
    @PutMapping("/api/{token}/reports/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable String token,
                                                @PathVariable int id,
                                                @RequestBody Report reportUpdate) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        Report report = reportRepository.getReportById(id);
        if (report == null) {
            throw new ResourceNotFoundException(String.format("Report not found with id=%d", id));
        }

        report.setSolved(reportUpdate.getSolved());
        return ResponseEntity.ok(reportRepository.save(report));
    }

    /**
     * API: Get all label templates for this admin's organisation.
     */
    @GetMapping("/api/{token}/templates")
    public ResponseEntity<List<LabelTemplate>> getTemplates(@PathVariable String token) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(labelTemplateRepository.findByOrgAdminId(admin.getId()));
    }

    /**
     * API: Create a new label template.
     */
    @PostMapping("/api/{token}/templates")
    public ResponseEntity<LabelTemplate> createTemplate(@PathVariable String token,
                                                         @RequestBody LabelTemplate template) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        template.setOrgAdmin(admin);
        LabelTemplate saved = labelTemplateRepository.save(template);
        return ResponseEntity.ok(saved);
    }

    /**
     * API: Update an existing label template.
     */
    @PutMapping("/api/{token}/templates/{templateId}")
    public ResponseEntity<LabelTemplate> updateTemplate(@PathVariable String token,
                                                         @PathVariable int templateId,
                                                         @RequestBody LabelTemplate updates) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        LabelTemplate existing = labelTemplateRepository.findById(templateId);
        if (existing == null || existing.getOrgAdmin().getId() != admin.getId()) {
            return ResponseEntity.status(404).build();
        }

        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getPrimaryColor() != null) existing.setPrimaryColor(updates.getPrimaryColor());
        if (updates.getSecondaryColor() != null) existing.setSecondaryColor(updates.getSecondaryColor());
        if (updates.getBackgroundColor() != null) existing.setBackgroundColor(updates.getBackgroundColor());
        if (updates.getFontFamily() != null) existing.setFontFamily(updates.getFontFamily());
        if (updates.getTitleFontSize() != null) existing.setTitleFontSize(updates.getTitleFontSize());
        if (updates.getBodyFontSize() != null) existing.setBodyFontSize(updates.getBodyFontSize());
        if (updates.getBorderStyle() != null) existing.setBorderStyle(updates.getBorderStyle());
        if (updates.getBorderColor() != null) existing.setBorderColor(updates.getBorderColor());
        if (updates.getLogoPosition() != null) existing.setLogoPosition(updates.getLogoPosition());
        existing.setShowQrCode(updates.isShowQrCode());
        if (updates.getHeaderText() != null) existing.setHeaderText(updates.getHeaderText());
        if (updates.getLayoutVariant() != null) existing.setLayoutVariant(updates.getLayoutVariant());

        return ResponseEntity.ok(labelTemplateRepository.save(existing));
    }

    /**
     * API: Delete a label template.
     */
    @DeleteMapping("/api/{token}/templates/{templateId}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String token,
                                                @PathVariable int templateId) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }

        LabelTemplate existing = labelTemplateRepository.findById(templateId);
        if (existing == null || existing.getOrgAdmin().getId() != admin.getId()) {
            return ResponseEntity.status(404).build();
        }

        labelTemplateRepository.delete(existing);
        return ResponseEntity.ok().build();
    }

    /**
     * Public: Get the template for a specific organisation (by org name).
     * Used by the label view/PDF generation.
     */
    @GetMapping("/templates/by-organisation")
    public ResponseEntity<List<LabelTemplate>> getTemplatesByOrganisation(@RequestParam String organisation) {
        List<OrgAdmin> admins = orgAdminRepository.findAll().stream()
                .filter(a -> a.getOrganisation().equalsIgnoreCase(organisation))
                .collect(Collectors.toList());
        if (admins.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<LabelTemplate> templates = labelTemplateRepository.findByOrgAdminId(admins.get(0).getId());
        return ResponseEntity.ok(templates);
    }
}
