package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.LegalResponsible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LegalResponsibleRepository extends JpaRepository<LegalResponsible, UUID> {

    List<LegalResponsible> findAllByOrganizationId(UUID organizationId);

    List<LegalResponsible> findAllByOrganizationIdAndIsActive(UUID organizationId, Boolean isActive);

    boolean existsByOrganizationIdAndEmail(UUID organizationId, String email);
}
