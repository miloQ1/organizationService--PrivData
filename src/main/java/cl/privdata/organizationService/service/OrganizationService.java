package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.OrganizationRequest;
import cl.privdata.organizationService.dto.response.OrganizationResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    List<OrganizationResponse> findAll();

    OrganizationResponse findById(UUID id);

    OrganizationResponse create(OrganizationRequest request);

    OrganizationResponse update(UUID id, OrganizationRequest request);

    void delete(UUID id);
}
