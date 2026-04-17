package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

import java.util.UUID;

public record PersonRequest(

        @NotNull(message = "El ID de la organización es obligatorio")
        UUID organizationId,

        UUID departmentId,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        String rut,

        @Email(message = "Formato de correo inválido")
        String email,

        String phone,

        String position,

        boolean isActive
) {}
