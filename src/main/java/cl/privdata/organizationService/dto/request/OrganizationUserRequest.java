package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrganizationUserRequest(

        @NotNull(message = "Organization ID is required")
        UUID organizationId,

        @NotNull(message = "User ID is required")
        UUID userId,

        UUID departmentId,

        String position,

        @NotNull(message = "isActive is required")
        Boolean isActive
) {}
