package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.OrganizationRequest;
import cl.privdata.organizationService.dto.response.OrganizationResponse;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.exception.BusinessRuleException;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository repository;

    public OrganizationService(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrganizationResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));
    }

    public OrganizationResponse create(OrganizationRequest request) {
        // Un RUT solo puede pertenecer a una organización
        if (repository.existsByRut(request.rut())) {
            throw new BusinessRuleException("An organization with RUT '" + request.rut() + "' already exists.");
        }
        Organization entity = new Organization();
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    public OrganizationResponse update(UUID id, OrganizationRequest request) {
        Organization entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));

        // Validar RUT solo si cambió
        boolean rutChanged = !entity.getRut().equals(request.rut());
        if (rutChanged && repository.existsByRut(request.rut())) {
            throw new BusinessRuleException("An organization with RUT '" + request.rut() + "' already exists.");
        }

        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    public void delete(UUID id) {
        Organization entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", id));

        // No se puede eliminar una organización activa
        if (Boolean.TRUE.equals(entity.getIsActive())) {
            throw new BusinessRuleException("Cannot delete an active organization. Deactivate it first.");
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
