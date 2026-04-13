package cl.privdata.organizationService.dto.response;

import java.time.LocalDateTime;

public record OrganizationResponse(
        Long id,
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
