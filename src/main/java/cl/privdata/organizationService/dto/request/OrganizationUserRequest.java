package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrganizationUserRequest(

        @NotNull(message = "El ID de la organización es obligatorio")
        Long organizationId,

        @NotNull(message = "El ID del usuario (Auth) es obligatorio")
        Long userId,

        Long departmentId,

        @NotBlank(message = "El cargo o posición es obligatorio")
        String position,

        boolean isActive
) {}
