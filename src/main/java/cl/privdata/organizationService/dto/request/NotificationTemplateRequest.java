package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationTemplateRequest(

        @NotNull(message = "El ID de la organización es obligatorio")
        Long organizationId,

        @NotBlank(message = "El código único es obligatorio")
        String code,

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        String templateType,

        @NotBlank(message = "El asunto de la plantilla es obligatorio")
        String subjectTemplate,

        @NotBlank(message = "El cuerpo de la plantilla es obligatorio")
        String bodyTemplate,

        boolean isActive
) {}
