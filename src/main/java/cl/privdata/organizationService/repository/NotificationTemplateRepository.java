package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, UUID> {

    List<NotificationTemplate> findAllByOrganizationId(UUID organizationId);

    List<NotificationTemplate> findAllByOrganizationIdAndIsActive(UUID organizationId, Boolean isActive);

    Optional<NotificationTemplate> findByOrganizationIdAndCodeAndVersion(UUID organizationId, String code, Integer version);

    List<NotificationTemplate> findAllByOrganizationIdAndCode(UUID organizationId, String code);
}
