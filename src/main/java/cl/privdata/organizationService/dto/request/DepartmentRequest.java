package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DepartmentRequest(

        @NotNull(message = "Organization ID is required")
        UUID organizationId,

        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotNull(message = "isActive is required")
        Boolean isActive
) {}
