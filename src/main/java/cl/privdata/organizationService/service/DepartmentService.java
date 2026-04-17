package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.DepartmentRequestDTO;
import cl.privdata.organizationService.dto.response.DepartmentResponseDTO;
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
    public List<DepartmentResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(entity -> {
                    DepartmentResponseDTO response = new DepartmentResponseDTO();
                    response.setId(entity.getId());
                    response.setOrganizationId(entity.getOrganization().getId());
                    response.setName(entity.getName());
                    response.setDescription(entity.getDescription());
                    response.setActive(entity.getIsActive());
                    response.setCreatedAt(entity.getCreatedAt());
                    return response;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(entity -> {
                    DepartmentResponseDTO response = new DepartmentResponseDTO();
                    response.setId(entity.getId());
                    response.setOrganizationId(entity.getOrganization().getId());
                    response.setName(entity.getName());
                    response.setDescription(entity.getDescription());
                    response.setActive(entity.getIsActive());
                    response.setCreatedAt(entity.getCreatedAt());
                    return response;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public DepartmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(entity -> {
                    DepartmentResponseDTO response = new DepartmentResponseDTO();
                    response.setId(entity.getId());
                    response.setOrganizationId(entity.getOrganization().getId());
                    response.setName(entity.getName());
                    response.setDescription(entity.getDescription());
                    response.setActive(entity.getIsActive());
                    response.setCreatedAt(entity.getCreatedAt());
                    return response;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    public DepartmentResponseDTO create(DepartmentRequestDTO request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        if (!Boolean.TRUE.equals(organization.getIsActive())) {
            throw new BusinessRuleException("Cannot add a department to an inactive organization.");
        }

        if (repository.existsByOrganizationIdAndName(request.getOrganizationId(), request.getName())) {
            throw new BusinessRuleException("Department name '" + request.getName() + "' already exists in this organization.");
        }

        Department entity = new Department();
        entity.setOrganization(organization);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setIsActive(request.isActive());
        
        Department saved = repository.saveAndFlush(entity);
        DepartmentResponseDTO response = new DepartmentResponseDTO();
        response.setId(saved.getId());
        response.setOrganizationId(saved.getOrganization().getId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setActive(saved.getIsActive());
        response.setCreatedAt(saved.getCreatedAt());
        return response;
    }

    public DepartmentResponseDTO update(UUID id, DepartmentRequestDTO request) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        boolean nameChanged = !entity.getName().equals(request.getName());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.getOrganizationId());
        if ((nameChanged || orgChanged) && repository.existsByOrganizationIdAndName(request.getOrganizationId(), request.getName())) {
            throw new BusinessRuleException("Department name '" + request.getName() + "' already exists in this organization.");
        }

        entity.setOrganization(organization);
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setIsActive(request.isActive());
        
        Department saved = repository.saveAndFlush(entity);
        DepartmentResponseDTO response = new DepartmentResponseDTO();
        response.setId(saved.getId());
        response.setOrganizationId(saved.getOrganization().getId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setActive(saved.getIsActive());
        response.setCreatedAt(saved.getCreatedAt());
        return response;
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

    public DepartmentResponseDTO deactivate(UUID id) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
        entity.setIsActive(false);
        
        Department saved = repository.saveAndFlush(entity);
        DepartmentResponseDTO response = new DepartmentResponseDTO();
        response.setId(saved.getId());
        response.setOrganizationId(saved.getOrganization().getId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setActive(saved.getIsActive());
        response.setCreatedAt(saved.getCreatedAt());
        return response;
    }
}
