package cl.privdata.organizationService.service.impl;

import cl.privdata.organizationService.dto.request.NotificationTemplateRequest;
import cl.privdata.organizationService.dto.response.NotificationTemplateResponse;
import cl.privdata.organizationService.entity.NotificationTemplate;
import cl.privdata.organizationService.entity.Organization;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.repository.NotificationTemplateRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import cl.privdata.organizationService.service.NotificationTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateRepository repository;
    private final OrganizationRepository organizationRepository;

    public NotificationTemplateServiceImpl(NotificationTemplateRepository repository,
                                            OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findAllByOrganization(UUID organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", id));
    }

    @Override
    public NotificationTemplateResponse create(NotificationTemplateRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));
        NotificationTemplate entity = new NotificationTemplate();
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public NotificationTemplateResponse update(UUID id, NotificationTemplateRequest request) {
        NotificationTemplate entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));
        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
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
