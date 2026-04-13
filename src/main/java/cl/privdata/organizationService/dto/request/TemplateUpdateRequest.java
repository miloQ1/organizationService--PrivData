package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TemplateUpdateRequest(

        String subjectTemplate,

        @NotBlank(message = "Body template is required")
        String bodyTemplate
) {}
