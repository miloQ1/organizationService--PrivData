package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.OrganizationUserRequest;
import cl.privdata.organizationService.dto.response.OrganizationUserResponse;
import cl.privdata.organizationService.entity.Department;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.entity.OrganizationUser;
import cl.privdata.organizationService.exception.BusinessRuleException;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.DepartmentRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.repository.OrganizationUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrganizationUserService {

    private final OrganizationUserRepository repository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;

    public OrganizationUserService(OrganizationUserRepository repository,
                                   OrganizationRepository organizationRepository,
                                   DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<OrganizationUserResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrganizationUserResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrganizationUserResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationUser", id));
    }

    public OrganizationUserResponse create(OrganizationUserRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // Solo se puede agregar usuarios a organizaciones activas
        if (!Boolean.TRUE.equals(organization.getIsActive())) {
            throw new BusinessRuleException("Cannot add a user to an inactive organization.");
        }

        // Un usuario no puede pertenecer dos veces a la misma organización
        if (repository.existsByOrganizationIdAndUserId(request.organizationId(), request.userId())) {
            throw new BusinessRuleException("User is already a member of this organization.");
        }

        OrganizationUser entity = new OrganizationUser();
        entity.setOrganization(organization);
        entity.setUserId(request.userId());

        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.departmentId()));

            // El departamento debe pertenecer a la misma organización
            if (!department.getOrganization().getId().equals(request.organizationId())) {
                throw new BusinessRuleException("Department does not belong to the specified organization.");
            }
            entity.setDepartment(department);
        }

        entity.setPosition(request.position());
        entity.setIsActive(request.isActive());
        return toResponse(repository.save(entity));
    }

    public OrganizationUserResponse update(UUID id, OrganizationUserRequest request) {
        OrganizationUser entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationUser", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // Validar unicidad de userId+orgId solo si alguno cambió
        boolean userChanged = !entity.getUserId().equals(request.userId());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.organizationId());
        if ((userChanged || orgChanged) && repository.existsByOrganizationIdAndUserId(request.organizationId(), request.userId())) {
            throw new BusinessRuleException("User is already a member of this organization.");
        }

        entity.setOrganization(organization);
        entity.setUserId(request.userId());

        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.departmentId()));

            // El departamento debe pertenecer a la misma organización
            if (!department.getOrganization().getId().equals(request.organizationId())) {
                throw new BusinessRuleException("Department does not belong to the specified organization.");
            }
            entity.setDepartment(department);
        } else {
            entity.setDepartment(null);
        }

        entity.setPosition(request.position());
        entity.setIsActive(request.isActive());
        return toResponse(repository.save(entity));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("OrganizationUser", id);
        }
        repository.deleteById(id);
    }

    // --- Mapping helpers ---

    private OrganizationUserResponse toResponse(OrganizationUser entity) {
        UUID departmentId = entity.getDepartment() != null ? entity.getDepartment().getId() : null;
        return new OrganizationUserResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getUserId(),
                departmentId,
                entity.getPosition(),
                entity.getIsActive(),
                entity.getJoinedAt()
        );
    }
}
