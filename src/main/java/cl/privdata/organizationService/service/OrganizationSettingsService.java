package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.OrganizationSettingsRequest;
import cl.privdata.organizationService.dto.response.OrganizationSettingsResponse;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.model.Organization;
import cl.privdata.organizationService.model.OrganizationSettings;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.repository.OrganizationSettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class OrganizationSettingsService {

    private final OrganizationSettingsRepository repository;
    private final OrganizationRepository organizationRepository;

    public OrganizationSettingsService(OrganizationSettingsRepository repository,
                                       OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }

    @Transactional(readOnly = true)
    public OrganizationSettingsResponse findByOrganization(UUID organizationId) {
        return repository.findByOrganizationId(organizationId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationSettings for Organization", organizationId));
    }

    // Upsert: crea si no existe, actualiza si ya existe
    public OrganizationSettingsResponse createOrUpdate(OrganizationSettingsRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        OrganizationSettings entity = repository.findByOrganizationId(request.organizationId())
                .orElse(new OrganizationSettings());

        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public void delete(UUID organizationId) {
        OrganizationSettings entity = repository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationSettings for Organization", organizationId));
        repository.delete(entity);
    }

    // --- Mapping helpers ---

    private void mapToEntity(OrganizationSettingsRequest request, OrganizationSettings entity) {
        if (request.defaultLanguage() != null) entity.setDefaultLanguage(request.defaultLanguage());
        entity.setPrivacyEmail(request.privacyEmail());
        entity.setAllowDataExports(request.allowDataExports());
    }

    private OrganizationSettingsResponse toResponse(OrganizationSettings entity) {
        return new OrganizationSettingsResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getDefaultLanguage(),
                entity.getPrivacyEmail(),
                entity.getAllowDataExports(),
                entity.getUpdatedAt()
        );
    }
}
