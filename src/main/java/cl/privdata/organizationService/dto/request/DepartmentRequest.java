package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(

        @NotNull(message = "El ID de la organización es obligatorio")
        Long organizationId,

        @NotBlank(message = "El nombre del departamento es obligatorio")
        String name,

        String description,

        boolean isActive
) {}
