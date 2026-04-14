package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationTemplateResponse(
        UUID id,
        UUID organizationId,
        String code,
        String name,
        String templateType,
        String subjectTemplate,
        String bodyTemplate,
        boolean isActive,
        Integer version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
