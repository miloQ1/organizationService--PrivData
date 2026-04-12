package cl.privdata.organizationService.service.impl;

import cl.privdata.organizationService.dto.request.OrganizationUserRequest;
import cl.privdata.organizationService.dto.response.OrganizationUserResponse;
import cl.privdata.organizationService.entity.Department;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.entity.OrganizationUser;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.DepartmentRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.repository.OrganizationUserRepository;
import cl.privdata.organizationService.service.OrganizationUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrganizationUserServiceImpl implements OrganizationUserService {

    private final OrganizationUserRepository repository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;

    public OrganizationUserServiceImpl(OrganizationUserRepository repository,
                                        OrganizationRepository organizationRepository,
                                        DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationUserResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationUserResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationUserResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationUser", id));
    }

    @Override
    public OrganizationUserResponse create(OrganizationUserRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        OrganizationUser entity = new OrganizationUser();
        entity.setOrganization(organization);
        entity.setUserId(request.userId());

        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.departmentId()));
            entity.setDepartment(department);
        }

        entity.setPosition(request.position());
        entity.setIsActive(request.isActive());

        return toResponse(repository.save(entity));
    }

    @Override
    public OrganizationUserResponse update(UUID id, OrganizationUserRequest request) {
        OrganizationUser entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationUser", id));

        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));
        entity.setOrganization(organization);
        entity.setUserId(request.userId());

        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", request.departmentId()));
            entity.setDepartment(department);
        } else {
            entity.setDepartment(null);
        }

        entity.setPosition(request.position());
        entity.setIsActive(request.isActive());

        return toResponse(repository.save(entity));
    }

    @Override
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
