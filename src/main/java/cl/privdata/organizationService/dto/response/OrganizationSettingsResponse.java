package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationSettingsResponse(
        UUID id,
        UUID organizationId,
        String defaultLanguage,
        Integer retentionPolicyDays,
        String privacyEmail,
        Boolean allowDataExports,
        Integer consentExpiryAlertDays,
        LocalDateTime updatedAt
) {}
