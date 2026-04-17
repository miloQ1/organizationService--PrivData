package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.OrganizationSettingsRequestDTO;
import cl.privdata.organizationService.dto.response.OrganizationSettingsResponseDTO;
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
    public OrganizationSettingsResponseDTO findByOrganization(UUID organizationId) {
        return repository.findByOrganizationId(organizationId)
                .map(entity -> {
                    OrganizationSettingsResponseDTO response = new OrganizationSettingsResponseDTO();
                    response.setId(entity.getId());
                    response.setOrganizationId(entity.getOrganization().getId());
                    response.setDefaultLanguage(entity.getDefaultLanguage());
                    response.setPrivacyEmail(entity.getPrivacyEmail());
                    response.setAllowDataExports(entity.getAllowDataExports());
                    response.setUpdatedAt(entity.getUpdatedAt());
                    return response;
                })
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationSettings for Organization", organizationId));
    }

    // Upsert: crea si no existe, actualiza si ya existe
    public OrganizationSettingsResponseDTO createOrUpdate(OrganizationSettingsRequestDTO request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        OrganizationSettings entity = repository.findByOrganizationId(request.getOrganizationId())
                .orElse(new OrganizationSettings());

        entity.setOrganization(organization);
        if (request.getDefaultLanguage() != null) entity.setDefaultLanguage(request.getDefaultLanguage());
        entity.setPrivacyEmail(request.getPrivacyEmail());
        entity.setAllowDataExports(request.isAllowDataExports());
        
        OrganizationSettings saved = repository.saveAndFlush(entity);
        OrganizationSettingsResponseDTO response = new OrganizationSettingsResponseDTO();
        response.setId(saved.getId());
        response.setOrganizationId(saved.getOrganization().getId());
        response.setDefaultLanguage(saved.getDefaultLanguage());
        response.setPrivacyEmail(saved.getPrivacyEmail());
        response.setAllowDataExports(saved.getAllowDataExports());
        response.setUpdatedAt(saved.getUpdatedAt());
        return response;
    }

    public void delete(UUID organizationId) {
        OrganizationSettings entity = repository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationSettings for Organization", organizationId));
        repository.delete(entity);
    }
}
