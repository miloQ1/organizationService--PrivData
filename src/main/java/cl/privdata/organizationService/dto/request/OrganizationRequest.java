package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrganizationRequest(

        @NotBlank(message = "Name is required")
        String name,

        String legalName,

        @NotBlank(message = "RUT is required")
        String rut,

        String businessType,

        @Email(message = "Invalid email format")
        String email,

        String phone,

        String address,

        @NotNull(message = "isActive is required")
        Boolean isActive
) {}
