package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.DepartmentRequest;
import cl.privdata.organizationService.dto.response.DepartmentResponse;
import cl.privdata.organizationService.exception.BusinessRuleException;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.model.Department;
import cl.privdata.organizationService.model.Organization;
import cl.privdata.organizationService.repository.DepartmentRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.repository.OrganizationUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository repository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationUserRepository organizationUserRepository;

    public DepartmentService(DepartmentRepository repository,
                             OrganizationRepository organizationRepository,
                             OrganizationUserRepository organizationUserRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
        this.organizationUserRepository = organizationUserRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAllByOrganization(Long organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    public DepartmentResponse create(DepartmentRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // Solo se pueden crear departamentos en organizaciones activas
        if (!Boolean.TRUE.equals(organization.getIsActive())) {
            throw new BusinessRuleException("Cannot add a department to an inactive organization.");
        }

        // El nombre del departamento debe ser único dentro de la organización
        if (repository.existsByOrganizationIdAndName(request.organizationId(), request.name())) {
            throw new BusinessRuleException("Department name '" + request.name() + "' already exists in this organization.");
        }

        Department entity = new Department();
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // Validar nombre único solo si cambió
        boolean nameChanged = !entity.getName().equals(request.name());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.organizationId());
        if ((nameChanged || orgChanged) && repository.existsByOrganizationIdAndName(request.organizationId(), request.name())) {
            throw new BusinessRuleException("Department name '" + request.name() + "' already exists in this organization.");
        }

        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public void delete(Long id) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));

        // No eliminar un departamento que tenga usuarios activos asignados
        boolean hasActiveUsers = !organizationUserRepository.findAllByDepartmentId(id).stream()
                .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                .toList()
                .isEmpty();
        if (hasActiveUsers) {
            throw new BusinessRuleException("Cannot delete a department with active users assigned.");
        }

        repository.delete(entity);
    }

    // Borrado lógico: desactiva el departamento sin eliminarlo
    public DepartmentResponse deactivate(Long id) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        entity.setIsActive(false);
        return toResponse(repository.saveAndFlush(entity));
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
