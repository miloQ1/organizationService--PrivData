package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByRut(String rut);

    Optional<Organization> findByEmail(String email);

    List<Organization> findAllByIsActive(Boolean isActive);

    boolean existsByRut(String rut);
}
