package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.DepartmentRequest;
import cl.privdata.organizationService.dto.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    List<DepartmentResponse> findAll();

    List<DepartmentResponse> findAllByOrganization(UUID organizationId);

    DepartmentResponse findById(UUID id);

    DepartmentResponse create(DepartmentRequest request);

    DepartmentResponse update(UUID id, DepartmentRequest request);

    void delete(UUID id);
}
