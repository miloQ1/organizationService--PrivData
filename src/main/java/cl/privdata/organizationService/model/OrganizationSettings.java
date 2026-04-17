package cl.privdata.organizationService.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "organization_settings")
public class OrganizationSettings {

    @Id
<<<<<<< HEAD:src/main/java/cl/privdata/organizationService/entity/OrganizationSettings.java
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
=======
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
>>>>>>> c456467ade925ba47fbe815d89ac6e2d3fcec8a5:src/main/java/cl/privdata/organizationService/model/OrganizationSettings.java
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false, unique = true)
    private Organization organization;

    @Column(name = "default_language", length = 20, nullable = false)
    private String defaultLanguage = "es";

    @Column(name = "privacy_email", length = 150)
    private String privacyEmail;

    @Column(name = "allow_data_exports", nullable = false)
    private Boolean allowDataExports = false;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public OrganizationSettings() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getDefaultLanguage() { return defaultLanguage; }
    public void setDefaultLanguage(String defaultLanguage) { this.defaultLanguage = defaultLanguage; }

    public String getPrivacyEmail() { return privacyEmail; }
    public void setPrivacyEmail(String privacyEmail) { this.privacyEmail = privacyEmail; }

    public Boolean getAllowDataExports() { return allowDataExports; }
    public void setAllowDataExports(Boolean allowDataExports) { this.allowDataExports = allowDataExports; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
