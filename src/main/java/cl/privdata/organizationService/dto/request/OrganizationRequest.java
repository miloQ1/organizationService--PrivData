package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrganizationRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "La razón social es obligatoria")
        String legalName,

        @NotBlank(message = "El RUT es obligatorio")
        String rut,

        String businessType,

        @Email(message = "Formato de correo inválido")
        String email,

        String phone,

        String address,

        boolean isActive
) {}
