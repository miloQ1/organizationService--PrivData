package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LegalResponsibleRequest(

        @NotNull(message = "Organization ID is required")
        UUID organizationId,

        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        String phone,

        String roleType,

        @NotNull(message = "isActive is required")
        Boolean isActive
) {}
