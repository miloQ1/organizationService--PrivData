package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationUserResponse(
        UUID id,
        UUID organizationId,
        Long userId,
        UUID departmentId,
        String position,
        boolean isActive,
        LocalDateTime joinedAt
) {}
