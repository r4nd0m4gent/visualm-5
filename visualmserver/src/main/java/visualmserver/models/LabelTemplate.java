package visualmserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "label_template")
public class LabelTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "background_color")
    private String backgroundColor;

    @Column(name = "font_family")
    private String fontFamily;

    @Column(name = "title_font_size")
    private String titleFontSize;

    @Column(name = "body_font_size")
    private String bodyFontSize;

    @Column(name = "border_style")
    private String borderStyle;

    @Column(name = "border_color")
    private String borderColor;

    @Column(name = "logo_position")
    private String logoPosition;

    @Column(name = "show_qr_code")
    private boolean showQrCode;

    @Column(name = "header_text")
    private String headerText;

    @Column(name = "layout_variant")
    private String layoutVariant;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "org_admin_id")
    private OrgAdmin orgAdmin;

    public LabelTemplate() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }

    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }

    public String getTitleFontSize() { return titleFontSize; }
    public void setTitleFontSize(String titleFontSize) { this.titleFontSize = titleFontSize; }

    public String getBodyFontSize() { return bodyFontSize; }
    public void setBodyFontSize(String bodyFontSize) { this.bodyFontSize = bodyFontSize; }

    public String getBorderStyle() { return borderStyle; }
    public void setBorderStyle(String borderStyle) { this.borderStyle = borderStyle; }

    public String getBorderColor() { return borderColor; }
    public void setBorderColor(String borderColor) { this.borderColor = borderColor; }

    public String getLogoPosition() { return logoPosition; }
    public void setLogoPosition(String logoPosition) { this.logoPosition = logoPosition; }

    public boolean isShowQrCode() { return showQrCode; }
    public void setShowQrCode(boolean showQrCode) { this.showQrCode = showQrCode; }

    public String getHeaderText() { return headerText; }
    public void setHeaderText(String headerText) { this.headerText = headerText; }

    public String getLayoutVariant() { return layoutVariant; }
    public void setLayoutVariant(String layoutVariant) { this.layoutVariant = layoutVariant; }

    public OrgAdmin getOrgAdmin() { return orgAdmin; }
    public void setOrgAdmin(OrgAdmin orgAdmin) { this.orgAdmin = orgAdmin; }
}
