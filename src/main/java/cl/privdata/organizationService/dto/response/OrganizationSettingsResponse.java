package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;

public record OrganizationSettingsResponse(
        Long id,
        Long organizationId,
        String defaultLanguage,
        Integer retentionPolicyDays,
        String privacyEmail,
        boolean allowDataExports,
        Integer consentExpiryAlertDays,
        LocalDateTime updatedAt
) {}
