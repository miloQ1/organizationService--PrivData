package cl.privdata.organizationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.privdata.organizationService.model.NotificationTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, UUID> {

    List<NotificationTemplate> findAllByOrganizationId(UUID organizationId);

    List<NotificationTemplate> findAllByOrganizationIdAndIsActive(UUID organizationId, Boolean isActive);

    Optional<NotificationTemplate> findByOrganizationIdAndCodeAndVersion(UUID organizationId, String code, Integer version);

    List<NotificationTemplate> findAllByOrganizationIdAndCode(UUID organizationId, String code);

    // Busca la versión activa más reciente de una plantilla por su código
    java.util.Optional<NotificationTemplate> findFirstByOrganizationIdAndCodeAndIsActiveTrueOrderByVersionDesc(UUID organizationId, String code);
}
