package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.OrganizationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, UUID> {

    List<OrganizationUser> findAllByOrganizationId(UUID organizationId);

    List<OrganizationUser> findAllByOrganizationIdAndIsActive(UUID organizationId, Boolean isActive);

    Optional<OrganizationUser> findByOrganizationIdAndUserId(UUID organizationId, UUID userId);

    List<OrganizationUser> findAllByDepartmentId(UUID departmentId);

    boolean existsByOrganizationIdAndUserId(UUID organizationId, UUID userId);
}
