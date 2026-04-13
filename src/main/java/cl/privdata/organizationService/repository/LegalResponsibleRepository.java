package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.LegalResponsible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalResponsibleRepository extends JpaRepository<LegalResponsible, Long> {

    List<LegalResponsible> findAllByOrganizationId(Long organizationId);

    List<LegalResponsible> findAllByOrganizationIdAndIsActive(Long organizationId, Boolean isActive);

    boolean existsByOrganizationIdAndEmail(Long organizationId, String email);
}
