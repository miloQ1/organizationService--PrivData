package cl.privdata.organizationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.privdata.organizationService.model.OrganizationSettings;

import java.util.Optional;

@Repository
public interface OrganizationSettingsRepository extends JpaRepository<OrganizationSettings, Long> {

    Optional<OrganizationSettings> findByOrganizationId(Long organizationId);

    boolean existsByOrganizationId(Long organizationId);
}
