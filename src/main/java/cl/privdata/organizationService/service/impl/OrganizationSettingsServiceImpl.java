package cl.privdata.organizationService.service.impl;

import cl.privdata.organizationService.dto.request.OrganizationSettingsRequest;
import cl.privdata.organizationService.dto.response.OrganizationSettingsResponse;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.entity.OrganizationSettings;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.repository.OrganizationSettingsRepository;
import cl.privdata.organizationService.service.OrganizationSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class OrganizationSettingsServiceImpl implements OrganizationSettingsService {

    private final OrganizationSettingsRepository repository;
    private final OrganizationRepository organizationRepository;

    public OrganizationSettingsServiceImpl(OrganizationSettingsRepository repository,
                                            OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationSettingsResponse findByOrganization(UUID organizationId) {
        return repository.findByOrganizationId(organizationId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationSettings for Organization", organizationId));
    }

    @Override
    public OrganizationSettingsResponse createOrUpdate(OrganizationSettingsRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        OrganizationSettings entity = repository.findByOrganizationId(request.organizationId())
                .orElse(new OrganizationSettings());

        entity.setOrganization(organization);
        mapToEntity(request, entity);

        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(UUID organizationId) {
        OrganizationSettings entity = repository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationSettings for Organization", organizationId));
        repository.delete(entity);
    }

    // --- Mapping helpers ---

    private void mapToEntity(OrganizationSettingsRequest request, OrganizationSettings entity) {
        if (request.defaultLanguage() != null) entity.setDefaultLanguage(request.defaultLanguage());
        entity.setRetentionPolicyDays(request.retentionPolicyDays());
        entity.setPrivacyEmail(request.privacyEmail());
        if (request.allowDataExports() != null) entity.setAllowDataExports(request.allowDataExports());
        entity.setConsentExpiryAlertDays(request.consentExpiryAlertDays());
    }

    private OrganizationSettingsResponse toResponse(OrganizationSettings entity) {
        return new OrganizationSettingsResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getDefaultLanguage(),
                entity.getRetentionPolicyDays(),
                entity.getPrivacyEmail(),
                entity.getAllowDataExports(),
                entity.getConsentExpiryAlertDays(),
                entity.getUpdatedAt()
        );
    }
}
