package cl.privdata.organizationService.service;

import cl.privdata.organizationService.dto.request.LegalResponsibleRequest;
import cl.privdata.organizationService.dto.response.LegalResponsibleResponse;

import java.util.List;
import java.util.UUID;

public interface LegalResponsibleService {

    List<LegalResponsibleResponse> findAll();

    List<LegalResponsibleResponse> findAllByOrganization(UUID organizationId);

    LegalResponsibleResponse findById(UUID id);

    LegalResponsibleResponse create(LegalResponsibleRequest request);

    LegalResponsibleResponse update(UUID id, LegalResponsibleRequest request);

    void delete(UUID id);
}
