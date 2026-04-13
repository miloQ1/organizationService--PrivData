package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    List<NotificationTemplate> findAllByOrganizationId(Long organizationId);

    List<NotificationTemplate> findAllByOrganizationIdAndIsActive(Long organizationId, Boolean isActive);

    Optional<NotificationTemplate> findByOrganizationIdAndCodeAndVersion(Long organizationId, String code, Integer version);

    List<NotificationTemplate> findAllByOrganizationIdAndCode(Long organizationId, String code);

    // Busca la versión activa más reciente de una plantilla por su código
    java.util.Optional<NotificationTemplate> findFirstByOrganizationIdAndCodeAndIsActiveTrueOrderByVersionDesc(Long organizationId, String code);
}
