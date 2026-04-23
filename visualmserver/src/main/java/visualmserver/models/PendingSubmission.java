package visualmserver.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pending_submission")
public class PendingSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "material_json", columnDefinition = "LONGTEXT")
    private String materialJson;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialJson() {
        return materialJson;
    }

    public void setMaterialJson(String materialJson) {
        this.materialJson = materialJson;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
