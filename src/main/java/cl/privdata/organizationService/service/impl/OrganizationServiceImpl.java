package cl.privdata.organizationService.service.impl;

import cl.privdata.organizationService.dto.request.OrganizationRequest;
import cl.privdata.organizationService.dto.response.OrganizationResponse;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.service.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository repository;

    public OrganizationServiceImpl(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));
    }

    @Override
    public OrganizationResponse create(OrganizationRequest request) {
        Organization entity = new Organization();
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public OrganizationResponse update(UUID id, OrganizationRequest request) {
        Organization entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Organization", id);
        }
        repository.deleteById(id);
    }

    // --- Mapping helpers ---

    private void mapToEntity(OrganizationRequest request, Organization entity) {
        entity.setName(request.name());
        entity.setLegalName(request.legalName());
        entity.setRut(request.rut());
        entity.setBusinessType(request.businessType());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setAddress(request.address());
        entity.setIsActive(request.isActive());
    }

    private OrganizationResponse toResponse(Organization entity) {
        return new OrganizationResponse(
                entity.getId(),
                entity.getName(),
                entity.getLegalName(),
                entity.getRut(),
                entity.getBusinessType(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
