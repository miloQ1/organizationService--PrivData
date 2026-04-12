package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrganizationSettingsRequest(

        @NotNull(message = "Organization ID is required")
        UUID organizationId,

        String defaultLanguage,

        Integer retentionPolicyDays,

        @Email(message = "Invalid privacy email format")
        String privacyEmail,

        Boolean allowDataExports,

        Integer consentExpiryAlertDays
) {}
