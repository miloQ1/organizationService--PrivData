package cl.privdata.organizationService.service.impl;

import cl.privdata.organizationService.dto.request.LegalResponsibleRequest;
import cl.privdata.organizationService.dto.response.LegalResponsibleResponse;
import cl.privdata.organizationService.entity.LegalResponsible;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.LegalResponsibleRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.service.LegalResponsibleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LegalResponsibleServiceImpl implements LegalResponsibleService {

    private final LegalResponsibleRepository repository;
    private final OrganizationRepository organizationRepository;

    public LegalResponsibleServiceImpl(LegalResponsibleRepository repository,
                                        OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalResponsibleResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalResponsibleResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LegalResponsibleResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("LegalResponsible", id));
    }

    @Override
    public LegalResponsibleResponse create(LegalResponsibleRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));
        LegalResponsible entity = new LegalResponsible();
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public LegalResponsibleResponse update(UUID id, LegalResponsibleRequest request) {
        LegalResponsible entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LegalResponsible", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("LegalResponsible", id);
        }
        repository.deleteById(id);
    }

    // --- Mapping helpers ---

    private void mapToEntity(LegalResponsibleRequest request, LegalResponsible entity) {
        entity.setFullName(request.fullName());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setRoleType(request.roleType());
        entity.setIsActive(request.isActive());
    }

    private LegalResponsibleResponse toResponse(LegalResponsible entity) {
        return new LegalResponsibleResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getRoleType(),
                entity.getIsActive(),
                entity.getCreatedAt()
        );
    }
}
