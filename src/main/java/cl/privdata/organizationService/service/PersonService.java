package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.PersonRequest;
import cl.privdata.organizationService.dto.response.PersonResponse;
import cl.privdata.organizationService.entity.Department;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.entity.Person;
import cl.privdata.organizationService.exception.BusinessRuleException;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.DepartmentRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PersonService {

    private final PersonRepository repository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;

    public PersonService(PersonRepository repository,
                         OrganizationRepository organizationRepository,
                         DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }

    public PersonResponse create(PersonRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        if (!Boolean.TRUE.equals(organization.getIsActive())) {
            throw new BusinessRuleException("Cannot add a person to an inactive organization.");
        }

        // Unicidad de RUT dentro de la organización (si se proporciona)
        if (request.rut() != null && !request.rut().isBlank()
                && repository.existsByOrganizationIdAndRut(request.organizationId(), request.rut())) {
            throw new BusinessRuleException("A person with RUT '" + request.rut() + "' already exists in this organization.");
        }

        // Unicidad de email dentro de la organización (si se proporciona)
        if (request.email() != null && !request.email().isBlank()
                && repository.existsByOrganizationIdAndEmail(request.organizationId(), request.email())) {
            throw new BusinessRuleException("A person with email '" + request.email() + "' already exists in this organization.");
        }

        Person entity = new Person();
        entity.setOrganization(organization);
        resolveAndSetDepartment(request, entity);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public PersonResponse update(UUID id, PersonRequest request) {
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // Validar RUT único solo si cambió
        boolean rutChanged = request.rut() != null && !request.rut().equals(entity.getRut());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.organizationId());
        if ((rutChanged || orgChanged) && request.rut() != null && !request.rut().isBlank()
                && repository.existsByOrganizationIdAndRut(request.organizationId(), request.rut())) {
            throw new BusinessRuleException("A person with RUT '" + request.rut() + "' already exists in this organization.");
        }

        // Validar email único solo si cambió
        boolean emailChanged = request.email() != null && !request.email().equals(entity.getEmail());
        if ((emailChanged || orgChanged) && request.email() != null && !request.email().isBlank()
                && repository.existsByOrganizationIdAndEmail(request.organizationId(), request.email())) {
            throw new BusinessRuleException("A person with email '" + request.email() + "' already exists in this organization.");
        }

        entity.setOrganization(organization);
        resolveAndSetDepartment(request, entity);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Person", id);
        }
        repository.deleteById(id);
    }

    public PersonResponse updateStatus(UUID id, Boolean isActive) {
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
        entity.setIsActive(isActive);
        return toResponse(repository.saveAndFlush(entity));
    }

    // --- Mapping helpers ---

    private void resolveAndSetDepartment(PersonRequest request, Person entity) {
        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.departmentId()));
            if (!department.getOrganization().getId().equals(request.organizationId())) {
                throw new BusinessRuleException("Department does not belong to the specified organization.");
            }
            entity.setDepartment(department);
        } else {
            entity.setDepartment(null);
        }
    }

    private void mapToEntity(PersonRequest request, Person entity) {
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setFullName(request.firstName() + " " + request.lastName());
        entity.setRut(request.rut());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setPosition(request.position());
        entity.setIsActive(request.isActive());
    }

    private PersonResponse toResponse(Person entity) {
        UUID departmentId = entity.getDepartment() != null ? entity.getDepartment().getId() : null;
        return new PersonResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                departmentId,
                entity.getFirstName(),
                entity.getLastName(),
                entity.getFullName(),
                entity.getRut(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPosition(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
