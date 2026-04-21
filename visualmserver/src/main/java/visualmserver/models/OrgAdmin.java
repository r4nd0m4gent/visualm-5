package visualmserver.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "org_admin")
public class OrgAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String organisation;

    @Column(name = "logo_path")
    private String logoPath;

    @NotNull
    @NotBlank
    @Column(name = "access_token", unique = true)
    private String accessToken;

    public OrgAdmin() {
    }

    public OrgAdmin(String email, String organisation, String logoPath, String accessToken) {
        this.email = email;
        this.organisation = organisation;
        this.logoPath = logoPath;
        this.accessToken = accessToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
