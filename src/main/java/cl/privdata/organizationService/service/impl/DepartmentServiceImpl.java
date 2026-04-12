package cl.privdata.organizationService.service.impl;

import cl.privdata.organizationService.dto.request.DepartmentRequest;
import cl.privdata.organizationService.dto.response.DepartmentResponse;
import cl.privdata.organizationService.entity.Department;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.DepartmentRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final OrganizationRepository organizationRepository;

    public DepartmentServiceImpl(DepartmentRepository repository,
                                  OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    @Override
    public DepartmentResponse create(DepartmentRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));
        Department entity = new Department();
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public DepartmentResponse update(UUID id, DepartmentRequest request) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Department", id);
        }
        repository.deleteById(id);
    }

    // --- Mapping helpers ---

    private void mapToEntity(DepartmentRequest request, Department entity) {
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setIsActive(request.isActive());
    }

    private DepartmentResponse toResponse(Department entity) {
        return new DepartmentResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getIsActive(),
                entity.getCreatedAt()
        );
    }
}
