package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonResponse(
        UUID id,
        UUID organizationId,
        UUID departmentId,
        String firstName,
        String lastName,
        String fullName,
        String rut,
        String email,
        String phone,
        String position,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
