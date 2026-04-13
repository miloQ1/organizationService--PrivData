package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;

public record LegalResponsibleResponse(
        Long id,
        Long organizationId,
        String fullName,
        String email,
        String phone,
        String roleType,
        boolean isActive,
        LocalDateTime createdAt
) {}
