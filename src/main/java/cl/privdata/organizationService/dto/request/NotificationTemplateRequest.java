package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NotificationTemplateRequest(

        @NotNull(message = "Organization ID is required")
        UUID organizationId,

        @NotBlank(message = "Code is required")
        String code,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Template type is required")
        String templateType,

        String subjectTemplate,

        @NotBlank(message = "Body template is required")
        String bodyTemplate,

        @NotNull(message = "isActive is required")
        Boolean isActive,

        @NotNull(message = "Version is required")
        Integer version
) {}
