package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LegalResponsibleRequest(

        @NotNull(message = "El ID de la organización es obligatorio")
        Long organizationId,

        @NotBlank(message = "El nombre completo es obligatorio")
        String fullName,

        @Email(message = "Formato de correo inválido")
        String email,

        String phone,

        @NotBlank(message = "El tipo de rol legal es obligatorio")
        String roleType,

        boolean isActive
) {}
