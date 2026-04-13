package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;

public record NotificationTemplateResponse(
        Long id,
        Long organizationId,
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
