package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.NotificationTemplateRequest;
import cl.privdata.organizationService.dto.request.TemplateRenderRequest;
import cl.privdata.organizationService.dto.request.TemplateUpdateRequest;
import cl.privdata.organizationService.dto.response.NotificationTemplateResponse;
import cl.privdata.organizationService.dto.response.TemplateRenderResponse;
import cl.privdata.organizationService.exception.BusinessRuleException;
import cl.privdata.organizationService.exception.ResourceNotFoundException;
import cl.privdata.organizationService.model.NotificationTemplate;
import cl.privdata.organizationService.model.Organization;
import cl.privdata.organizationService.repository.NotificationTemplateRepository;
import cl.privdata.organizationService.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    public List<NotificationTemplateResponse> findAllByOrganization(Long organizationId) {
        return repository.findAllByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificationTemplateResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", id));
    }

    public NotificationTemplateResponse create(NotificationTemplateRequest request) {
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // No puede existir ya una plantilla activa con el mismo código en la organización
        if (repository.findFirstByOrganizationIdAndCodeAndIsActiveTrueOrderByVersionDesc(
                request.organizationId(), request.code()).isPresent()) {
            throw new BusinessRuleException("Ya existe una plantilla activa con el código '" + request.code() + "' en esta organización.");
        }

        NotificationTemplate entity = new NotificationTemplate();
        entity.setOrganization(organization);
        entity.setVersion(1); // la versión inicial siempre es 1, el sistema la gestiona
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public NotificationTemplateResponse update(Long id, NotificationTemplateRequest request) {
        NotificationTemplate entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", id));
        Organization organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.organizationId()));

        // Validar que el nuevo código no colisione con otra plantilla activa distinta
        boolean codeChanged = !entity.getCode().equals(request.code());
        boolean orgChanged = !entity.getOrganization().getId().equals(request.organizationId());
        if (codeChanged || orgChanged) {
            repository.findFirstByOrganizationIdAndCodeAndIsActiveTrueOrderByVersionDesc(
                    request.organizationId(), request.code())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(entity.getId())) {
                            throw new BusinessRuleException("Ya existe una plantilla activa con el código '" + request.code() + "' en esta organización.");
                        }
                    });
        }

        entity.setOrganization(organization);
        mapToEntity(request, entity);
        return toResponse(repository.saveAndFlush(entity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("NotificationTemplate", id);
        }
        repository.deleteById(id);
    }

    // Obtiene la versión activa más reciente de una plantilla por su código
    @Transactional(readOnly = true)
    public NotificationTemplateResponse findActiveByCode(Long organizationId, String code) {
        return repository.findFirstByOrganizationIdAndCodeAndIsActiveTrueOrderByVersionDesc(organizationId, code)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate with code '" + code + "'", organizationId));
    }

    // Permite al administrador editar subject y body de la plantilla activa
    public NotificationTemplateResponse updateByCode(Long organizationId, String code, TemplateUpdateRequest request) {
        NotificationTemplate entity = repository
                .findFirstByOrganizationIdAndCodeAndIsActiveTrueOrderByVersionDesc(organizationId, code)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate with code '" + code + "'", organizationId));
        entity.setSubjectTemplate(request.subjectTemplate());
        entity.setBodyTemplate(request.bodyTemplate());
        return toResponse(repository.saveAndFlush(entity));
    }

    // Procesa la plantilla activa sustituyendo variables {{clave}} con los valores recibidos
    @Transactional(readOnly = true)
    public TemplateRenderResponse render(Long organizationId, String code, TemplateRenderRequest request) {
        NotificationTemplate template = repository
                .findFirstByOrganizationIdAndCodeAndIsActiveTrueOrderByVersionDesc(organizationId, code)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate with code '" + code + "'", organizationId));
        String subject = applyVariables(template.getSubjectTemplate(), request.variables());
        String body = applyVariables(template.getBodyTemplate(), request.variables());
        return new TemplateRenderResponse(subject, body);
    }

    private String applyVariables(String template, Map<String, String> variables) {
        if (template == null) return null;
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    // --- Mapping helpers ---

    private void mapToEntity(NotificationTemplateRequest request, NotificationTemplate entity) {
        entity.setCode(request.code());
        entity.setName(request.name());
        entity.setTemplateType(request.templateType());
        entity.setSubjectTemplate(request.subjectTemplate());
        entity.setBodyTemplate(request.bodyTemplate());
        entity.setIsActive(request.isActive());
        // version no se toma del request: se asigna en create() y no cambia en update()
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
