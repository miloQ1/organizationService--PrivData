package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.NotificationTemplateRequest;
import cl.privdata.organizationService.dto.response.NotificationTemplateResponse;
import cl.privdata.organizationService.entity.NotificationTemplate;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.exception.BusinessRuleException;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.NotificationTemplateRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationTemplateService {

    private final NotificationTemplateRepository repository;
    private final OrganizationRepository organizationRepository;

    public NotificationTemplateService(NotificationTemplateRepository repository,
                                       OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificationTemplateResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", id));
    }

    public NotificationTemplateResponse create(NotificationTemplateRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // La combinación code+version debe ser única por organización
        if (repository.findByOrganizationIdAndCodeAndVersion(
                request.organizationId(), request.code(), request.version()).isPresent()) {
            throw new BusinessRuleException("A template with code '" + request.code() +
                    "' and version " + request.version() + " already exists for this organization.");
        }

        NotificationTemplate entity = new NotificationTemplate();
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    public NotificationTemplateResponse update(UUID id, NotificationTemplateRequest request) {
        NotificationTemplate entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // Validar unicidad code+version solo si cambiaron
        boolean codeChanged = !entity.getCode().equals(request.code());
        boolean versionChanged = !entity.getVersion().equals(request.version());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.organizationId());
        if ((codeChanged || versionChanged || orgChanged) &&
                repository.findByOrganizationIdAndCodeAndVersion(
                        request.organizationId(), request.code(), request.version()).isPresent()) {
            throw new BusinessRuleException("A template with code '" + request.code() +
                    "' and version " + request.version() + " already exists for this organization.");
        }

        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("NotificationTemplate", id);
        }
        repository.deleteById(id);
    }

    // --- Mapping helpers ---

    private void mapToEntity(NotificationTemplateRequest request, NotificationTemplate entity) {
        entity.setCode(request.code());
        entity.setName(request.name());
        entity.setTemplateType(request.templateType());
        entity.setSubjectTemplate(request.subjectTemplate());
        entity.setBodyTemplate(request.bodyTemplate());
        entity.setIsActive(request.isActive());
        entity.setVersion(request.version());
    }

    private NotificationTemplateResponse toResponse(NotificationTemplate entity) {
        return new NotificationTemplateResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getCode(),
                entity.getName(),
                entity.getTemplateType(),
                entity.getSubjectTemplate(),
                entity.getBodyTemplate(),
                entity.getIsActive(),
                entity.getVersion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
