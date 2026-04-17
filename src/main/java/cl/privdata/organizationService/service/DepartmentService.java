package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.DepartmentRequest;
import cl.privdata.organizationService.dto.response.DepartmentResponse;
import cl.privdata.organizationService.exception.BusinessRuleException;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.model.Department;
import cl.privdata.organizationService.model.Organization;
import cl.privdata.organizationService.repository.DepartmentRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository repository;
    private final OrganizationRepository organizationRepository;
    private final PersonRepository personRepository;

    public DepartmentService(DepartmentRepository repository,
                             OrganizationRepository organizationRepository,
                             PersonRepository personRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DepartmentResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    public DepartmentResponse create(DepartmentRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        if (!Boolean.TRUE.equals(organization.getIsActive())) {
            throw new BusinessRuleException("Cannot add a department to an inactive organization.");
        }

        if (repository.existsByOrganizationIdAndName(request.organizationId(), request.name())) {
            throw new BusinessRuleException("Department name '" + request.name() + "' already exists in this organization.");
        }

        Department entity = new Department();
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public DepartmentResponse update(UUID id, DepartmentRequest request) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        boolean nameChanged = !entity.getName().equals(request.name());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.organizationId());
        if ((nameChanged || orgChanged) && repository.existsByOrganizationIdAndName(request.organizationId(), request.name())) {
            throw new BusinessRuleException("Department name '" + request.name() + "' already exists in this organization.");
        }

        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public void delete(UUID id) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));

        boolean hasActivePersons = personRepository.findAllByDepartmentId(id).stream()
                .anyMatch(p -> Boolean.TRUE.equals(p.getIsActive()));
        if (hasActivePersons) {
            throw new BusinessRuleException("Cannot delete a department with active persons assigned.");
        }

        repository.delete(entity);
    }

<<<<<<< HEAD
    // Borrado lógico: desactiva el departamento sin eliminarlo
=======
>>>>>>> 7d359ed (refactor, modelo nuevo)
    public DepartmentResponse deactivate(UUID id) {
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
