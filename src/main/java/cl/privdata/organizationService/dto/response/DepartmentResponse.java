package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record DepartmentResponse(
        UUID id,
        UUID organizationId,
        String name,
        String description,
        Boolean isActive,
        LocalDateTime createdAt
) {}
