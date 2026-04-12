package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record LegalResponsibleResponse(
        UUID id,
        UUID organizationId,
        String fullName,
        String email,
        String phone,
        String roleType,
        Boolean isActive,
        LocalDateTime createdAt
) {}
