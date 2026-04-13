package visualmserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import visualmserver.models.OrgAdmin;
import visualmserver.repositories.OrgAdminRepository;

@RestController
@RequestMapping("/org-admin")
public class OrgAdminController {

    @Autowired
    private OrgAdminRepository orgAdminRepository;

    /**
     * Validate a private access token and return the org admin details.
     * This is the entry point for the admin control panel.
     */
    @GetMapping("/access/{token}")
    public ResponseEntity<OrgAdmin> accessPanel(@PathVariable String token) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(admin);
    }

    /**
     * Update organisation details (name, logo) via private token.
     */
    @PutMapping("/access/{token}")
    public ResponseEntity<OrgAdmin> updateOrg(@PathVariable String token, @RequestBody OrgAdmin updates) {
        OrgAdmin admin = orgAdminRepository.findByAccessToken(token);
        if (admin == null) {
            return ResponseEntity.status(403).build();
        }
        if (updates.getOrganisation() != null) {
            admin.setOrganisation(updates.getOrganisation());
        }
        if (updates.getLogoPath() != null) {
            admin.setLogoPath(updates.getLogoPath());
        }
        orgAdminRepository.save(admin);
        return ResponseEntity.ok(admin);
    }
}
