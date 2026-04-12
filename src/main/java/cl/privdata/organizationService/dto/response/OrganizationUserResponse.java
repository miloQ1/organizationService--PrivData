package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationUserResponse(
        UUID id,
        UUID organizationId,
        UUID userId,
        UUID departmentId,
        String position,
        Boolean isActive,
        LocalDateTime joinedAt
) {}
