package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.OrganizationSettingsRequest;
import cl.privdata.organizationService.dto.response.OrganizationSettingsResponse;

import java.util.UUID;

public interface OrganizationSettingsService {

    OrganizationSettingsResponse findByOrganization(UUID organizationId);

    OrganizationSettingsResponse createOrUpdate(OrganizationSettingsRequest request);

    void delete(UUID organizationId);
}
