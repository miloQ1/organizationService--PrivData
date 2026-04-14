package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationResponse(
        UUID id,
        String name,
        String legalName,
        String rut,
        String businessType,
        String email,
        String phone,
        String address,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
