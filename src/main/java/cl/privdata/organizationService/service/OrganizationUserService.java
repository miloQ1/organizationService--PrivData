package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.OrganizationUserRequest;
import cl.privdata.organizationService.dto.response.OrganizationUserResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationUserService {

    List<OrganizationUserResponse> findAll();

    List<OrganizationUserResponse> findAllByOrganization(UUID organizationId);

    OrganizationUserResponse findById(UUID id);

    OrganizationUserResponse create(OrganizationUserRequest request);

    OrganizationUserResponse update(UUID id, OrganizationUserRequest request);

    void delete(UUID id);
}
