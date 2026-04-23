package visualmserver.models;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "material")
public class Material {
    public static final int MAXIMUM_STEPS = 10;
    public static final int MAXIMUM_INGREDIENTS = 6;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sequence_number")
    private Long sequenceNumber;

    @Column(name = "sequence_number_published")
    private Long sequenceNumberPublished;

    @Size(max = 25)
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @NotNull
    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDate creationDate;

    @NotNull
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String steps;

    @NotNull
    @NotBlank
    @Size(max = 200)
    @Column(columnDefinition = "TEXT")
    private String changes;

    @NotNull
    @NotBlank
    private String reference;

    @Column(name = "reference_email")
    private String referenceEmail;

    @Column(name = "overview_url")
    private String overviewURL;

    @Column(name = "close_up_url")
    private String closeUpURL;

    @NotNull
    @NotBlank
    @Column(name = "qrcode_url")
    private String qrCodeURL;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "material_has_tag",
            joinColumns = @JoinColumn(name = "sequence_number"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<Tag> tags;

    @NotNull
    @OneToMany(mappedBy = "material", cascade = {CascadeType.REMOVE})
    private Set<MaterialIngredient> materialIngredients;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SaveStatus saveStatus;

    @Enumerated(EnumType.STRING)
    private MaterialType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "parent_sequence_number")
    private Long parentId;

    @Column(name = "organisation")
    private String organisation;

    @Column(name = "post_processing_tags", columnDefinition = "TEXT")
    private String postProcessingTags;

    public Material() {
    }

    public Long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public Long getSequenceNumberPublished() {
        return this.sequenceNumberPublished;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setSequenceNumberPublished(Long sequenceNumberPublished) {
        this.sequenceNumberPublished = sequenceNumberPublished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getOverviewURL() {
        return overviewURL;
    }

    public void setOverviewURL(String mediaURL) {
        this.overviewURL = mediaURL;
    }

    public String getQrCodeURL() {
        return qrCodeURL;
    }

    public void setQrCodeURL(String qrCodeURL) {
        this.qrCodeURL = qrCodeURL;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.addMaterial(this);
    }

    public SaveStatus getSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(SaveStatus saveStatus) {
        this.saveStatus = saveStatus;
    }

    public MaterialType getType() {
        return type;
    }

    public void setType(MaterialType type) {
        this.type = type;
    }

    public Set<MaterialIngredient> getMaterialIngredients() {
        return materialIngredients;
    }

    public void setMaterialIngredients(Set<MaterialIngredient> materialIngredients) {
        this.materialIngredients = materialIngredients;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getPostProcessingTags() {
        return postProcessingTags;
    }

    public void setPostProcessingTags(String postProcessingTags) {
        this.postProcessingTags = postProcessingTags;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceEmail() {
        return referenceEmail;
    }

    public void setReferenceEmail(String referenceEmail) {
        this.referenceEmail = referenceEmail;
    }

    public String getCloseUpURL() {
        return closeUpURL;
    }

    public void setCloseUpURL(String closeUpURL) {
        this.closeUpURL = closeUpURL;
    }
}
