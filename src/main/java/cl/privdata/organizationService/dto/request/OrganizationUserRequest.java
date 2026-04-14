package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrganizationUserRequest(

        @NotNull(message = "El ID de la organización es obligatorio")
        UUID organizationId,

        @NotNull(message = "El ID del usuario (Auth) es obligatorio")
        Long userId,

        UUID departmentId,

        @NotBlank(message = "El cargo o posición es obligatorio")
        String position,

        boolean isActive
) {}
