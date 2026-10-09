package visualmserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import visualmserver.models.Material;
import visualmserver.models.OrgAdmin;
import visualmserver.models.User;
import visualmserver.repositories.OrgAdminRepository;
import visualmserver.repositories.UserRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Emails the admins of a label's organisation when a label is submitted for approval.
 * Works for anonymous submissions too: the organisation comes from the label itself.
 */
@Service
public class SubmissionNotifier {

    @Autowired
    private OrgAdminRepository orgAdminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Value("${frontend.url:}")
    private String frontendUrl;

    public void notifyAdmins(Material material) {
        String organisation = this.resolveOrganisation(material);
        String labelName = HtmlUtils.htmlEscape(material.getName() != null ? material.getName() : "Untitled");
        String orgText = organisation != null ? " for <strong>" + HtmlUtils.htmlEscape(organisation) + "</strong>" : "";
        String intro = "<p>A new label <strong>" + labelName + "</strong> has been submitted for review" + orgText + ".</p>";

        // Org admins manage approvals through their private panel link
        Set<String> notified = new LinkedHashSet<>();
        for (OrgAdmin admin : this.findOrgAdmins(organisation)) {
            if (admin.getEmail() == null || !notified.add(admin.getEmail().toLowerCase())) {
                continue;
            }
            String panelLink = this.baseUrl() + "/#/org-admin/access/" + admin.getAccessToken();
            emailSenderService.sendMail("Visualm5", admin.getEmail(), "Label submitted for approval",
                    intro + "<p><a href='" + panelLink + "'>Review it in your admin panel</a></p>");
        }

        // Admin user accounts that opted in to submission emails
        if (organisation != null) {
            for (User admin : userRepository.findByOrganisationAndAdmin(organisation, true)) {
                if (admin.isNotifyOnSubmission() && admin.getEmail() != null && notified.add(admin.getEmail().toLowerCase())) {
                    emailSenderService.sendMail("Visualm5", admin.getEmail(), "Label submitted for approval", intro);
                }
            }
        }
    }

    private String resolveOrganisation(Material material) {
        if (material.getOrganisation() != null && !material.getOrganisation().isBlank()) {
            return material.getOrganisation();
        }
        if (material.getUser() != null && material.getUser().getOrganisation() != null) {
            return material.getUser().getOrganisation();
        }
        return null;
    }

    /**
     * Admins of the label's organisation; every org admin when the organisation is unknown or has no admin,
     * so a submission never goes unnoticed.
     */
    private List<OrgAdmin> findOrgAdmins(String organisation) {
        List<OrgAdmin> all = orgAdminRepository.findAll();
        if (organisation == null) {
            return all;
        }
        List<OrgAdmin> matching = all.stream()
                .filter(a -> organisation.equalsIgnoreCase(a.getOrganisation()))
                .collect(Collectors.toList());
        return matching.isEmpty() ? all : matching;
    }

    private String baseUrl() {
        if (frontendUrl == null || frontendUrl.isBlank()) {
            return "";
        }
        return frontendUrl.endsWith("/") ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
    }
}
