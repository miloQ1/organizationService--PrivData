package cl.privdata.organizationService.dto.request;

import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(

        @NotNull(message = "isActive is required")
        Boolean isActive
) {}
