package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.NotificationTemplateRequest;
import cl.privdata.organizationService.dto.response.NotificationTemplateResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationTemplateService {

    List<NotificationTemplateResponse> findAll();

    List<NotificationTemplateResponse> findAllByOrganization(UUID organizationId);

    NotificationTemplateResponse findById(UUID id);

    NotificationTemplateResponse create(NotificationTemplateRequest request);

    NotificationTemplateResponse update(UUID id, NotificationTemplateRequest request);

    void delete(UUID id);
}
