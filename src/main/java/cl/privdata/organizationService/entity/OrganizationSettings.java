package cl.privdata.organizationService.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization_settings")
public class OrganizationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false, unique = true)
    private Organization organization;

    @Column(name = "default_language", length = 10)
    private String defaultLanguage = "es";

    @Column(name = "retention_policy_days")
    private Integer retentionPolicyDays;

    @Column(name = "privacy_email", length = 255)
    private String privacyEmail;

    @Column(name = "allow_data_exports", nullable = false)
    private Boolean allowDataExports = false;

    @Column(name = "consent_expiry_alert_days")
    private Integer consentExpiryAlertDays;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public OrganizationSettings() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getDefaultLanguage() { return defaultLanguage; }
    public void setDefaultLanguage(String defaultLanguage) { this.defaultLanguage = defaultLanguage; }

    public Integer getRetentionPolicyDays() { return retentionPolicyDays; }
    public void setRetentionPolicyDays(Integer retentionPolicyDays) { this.retentionPolicyDays = retentionPolicyDays; }

    public String getPrivacyEmail() { return privacyEmail; }
    public void setPrivacyEmail(String privacyEmail) { this.privacyEmail = privacyEmail; }

    public Boolean getAllowDataExports() { return allowDataExports; }
    public void setAllowDataExports(Boolean allowDataExports) { this.allowDataExports = allowDataExports; }

    public Integer getConsentExpiryAlertDays() { return consentExpiryAlertDays; }
    public void setConsentExpiryAlertDays(Integer consentExpiryAlertDays) { this.consentExpiryAlertDays = consentExpiryAlertDays; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
