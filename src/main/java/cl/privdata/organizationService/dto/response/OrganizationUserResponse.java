package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;

public record OrganizationUserResponse(
        Long id,
        Long organizationId,
        Long userId,
        Long departmentId,
        String position,
        boolean isActive,
        LocalDateTime joinedAt
) {}
