package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;

public record DepartmentResponse(
        Long id,
        Long organizationId,
        String name,
        String description,
        boolean isActive,
        LocalDateTime createdAt
) {}
