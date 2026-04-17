package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.PersonRequestDTO;
import cl.privdata.organizationService.dto.response.PersonResponseDTO;
import cl.privdata.organizationService.model.Department;
import cl.privdata.organizationService.model.Organization;
import cl.privdata.organizationService.model.Person;
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
    public List<PersonResponseDTO> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(entity -> {
                    UUID departmentId = entity.getDepartment() != null ? entity.getDepartment().getId() : null;
                    PersonResponseDTO response = new PersonResponseDTO();
                    response.setId(entity.getId());
                    response.setOrganizationId(entity.getOrganization().getId());
                    response.setDepartmentId(departmentId);
                    response.setFirstName(entity.getFirstName());
                    response.setLastName(entity.getLastName());
                    response.setFullName(entity.getFullName());
                    response.setRut(entity.getRut());
                    response.setEmail(entity.getEmail());
                    response.setPhone(entity.getPhone());
                    response.setPosition(entity.getPosition());
                    response.setActive(entity.getIsActive());
                    response.setCreatedAt(entity.getCreatedAt());
                    response.setUpdatedAt(entity.getUpdatedAt());
                    return response;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(entity -> {
                    UUID departmentId = entity.getDepartment() != null ? entity.getDepartment().getId() : null;
                    PersonResponseDTO response = new PersonResponseDTO();
                    response.setId(entity.getId());
                    response.setOrganizationId(entity.getOrganization().getId());
                    response.setDepartmentId(departmentId);
                    response.setFirstName(entity.getFirstName());
                    response.setLastName(entity.getLastName());
                    response.setFullName(entity.getFullName());
                    response.setRut(entity.getRut());
                    response.setEmail(entity.getEmail());
                    response.setPhone(entity.getPhone());
                    response.setPosition(entity.getPosition());
                    response.setActive(entity.getIsActive());
                    response.setCreatedAt(entity.getCreatedAt());
                    response.setUpdatedAt(entity.getUpdatedAt());
                    return response;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }

    public PersonResponseDTO create(PersonRequestDTO request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        if (!Boolean.TRUE.equals(organization.getIsActive())) {
            throw new BusinessRuleException("Cannot add a person to an inactive organization.");
        }

        // Unicidad de RUT dentro de la organización (si se proporciona)
        if (request.getRut() != null && !request.getRut().isBlank()
                && repository.existsByOrganizationIdAndRut(request.getOrganizationId(), request.getRut())) {
            throw new BusinessRuleException("A person with RUT '" + request.getRut() + "' already exists in this organization.");
        }

        // Unicidad de email dentro de la organización (si se proporciona)
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && repository.existsByOrganizationIdAndEmail(request.getOrganizationId(), request.getEmail())) {
            throw new BusinessRuleException("A person with email '" + request.getEmail() + "' already exists in this organization.");
        }

        Person entity = new Person();
        entity.setOrganization(organization);
        resolveAndSetDepartment(request, entity);
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setFullName(request.getFirstName() + " " + request.getLastName());
        entity.setRut(request.getRut());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setPosition(request.getPosition());
        entity.setIsActive(request.isActive());
        
        Person saved = repository.saveAndFlush(entity);
        UUID departmentId = saved.getDepartment() != null ? saved.getDepartment().getId() : null;
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(saved.getId());
        response.setOrganizationId(saved.getOrganization().getId());
        response.setDepartmentId(departmentId);
        response.setFirstName(saved.getFirstName());
        response.setLastName(saved.getLastName());
        response.setFullName(saved.getFullName());
        response.setRut(saved.getRut());
        response.setEmail(saved.getEmail());
        response.setPhone(saved.getPhone());
        response.setPosition(saved.getPosition());
        response.setActive(saved.getIsActive());
        response.setCreatedAt(saved.getCreatedAt());
        response.setUpdatedAt(saved.getUpdatedAt());
        return response;
    }

    public PersonResponseDTO update(UUID id, PersonRequestDTO request) {
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        // Validar RUT único solo si cambió
        boolean rutChanged = request.getRut() != null && !request.getRut().equals(entity.getRut());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.getOrganizationId());
        if ((rutChanged || orgChanged) && request.getRut() != null && !request.getRut().isBlank()
                && repository.existsByOrganizationIdAndRut(request.getOrganizationId(), request.getRut())) {
            throw new BusinessRuleException("A person with RUT '" + request.getRut() + "' already exists in this organization.");
        }

        // Validar email único solo si cambió
        boolean emailChanged = request.getEmail() != null && !request.getEmail().equals(entity.getEmail());
        if ((emailChanged || orgChanged) && request.getEmail() != null && !request.getEmail().isBlank()
                && repository.existsByOrganizationIdAndEmail(request.getOrganizationId(), request.getEmail())) {
            throw new BusinessRuleException("A person with email '" + request.getEmail() + "' already exists in this organization.");
        }

        entity.setOrganization(organization);
        resolveAndSetDepartment(request, entity);
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setFullName(request.getFirstName() + " " + request.getLastName());
        entity.setRut(request.getRut());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setPosition(request.getPosition());
        entity.setIsActive(request.isActive());
        
        Person saved = repository.saveAndFlush(entity);
        UUID departmentId = saved.getDepartment() != null ? saved.getDepartment().getId() : null;
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(saved.getId());
        response.setOrganizationId(saved.getOrganization().getId());
        response.setDepartmentId(departmentId);
        response.setFirstName(saved.getFirstName());
        response.setLastName(saved.getLastName());
        response.setFullName(saved.getFullName());
        response.setRut(saved.getRut());
        response.setEmail(saved.getEmail());
        response.setPhone(saved.getPhone());
        response.setPosition(saved.getPosition());
        response.setActive(saved.getIsActive());
        response.setCreatedAt(saved.getCreatedAt());
        response.setUpdatedAt(saved.getUpdatedAt());
        return response;
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Person", id);
        }
        repository.deleteById(id);
    }

    public PersonResponseDTO updateStatus(UUID id, Boolean isActive) {
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
        entity.setIsActive(isActive);
        
        Person saved = repository.saveAndFlush(entity);
        UUID departmentId = saved.getDepartment() != null ? saved.getDepartment().getId() : null;
        PersonResponseDTO response = new PersonResponseDTO();
        response.setId(saved.getId());
        response.setOrganizationId(saved.getOrganization().getId());
        response.setDepartmentId(departmentId);
        response.setFirstName(saved.getFirstName());
        response.setLastName(saved.getLastName());
        response.setFullName(saved.getFullName());
        response.setRut(saved.getRut());
        response.setEmail(saved.getEmail());
        response.setPhone(saved.getPhone());
        response.setPosition(saved.getPosition());
        response.setActive(saved.getIsActive());
        response.setCreatedAt(saved.getCreatedAt());
        response.setUpdatedAt(saved.getUpdatedAt());
        return response;
    }

    // --- Business logic helper ---

    private void resolveAndSetDepartment(PersonRequestDTO request, Person entity) {
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.getDepartmentId()));
            if (!department.getOrganization().getId().equals(request.getOrganizationId())) {
                throw new BusinessRuleException("Department does not belong to the specified organization.");
            }
            entity.setDepartment(department);
        } else {
            entity.setDepartment(null);
        }
    }
}
