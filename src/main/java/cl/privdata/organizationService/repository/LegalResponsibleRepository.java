package cl.privdata.organizationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.privdata.organizationService.model.LegalResponsible;

import java.util.List;

@Repository
public interface LegalResponsibleRepository extends JpaRepository<LegalResponsible, Long> {

    List<LegalResponsible> findAllByOrganizationId(Long organizationId);

    List<LegalResponsible> findAllByOrganizationIdAndIsActive(Long organizationId, Boolean isActive);

    boolean existsByOrganizationIdAndEmail(Long organizationId, String email);
}
