package cl.privdata.organizationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.privdata.organizationService.model.OrganizationUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, Long> {

    List<OrganizationUser> findAllByOrganizationId(Long organizationId);

    List<OrganizationUser> findAllByOrganizationIdAndIsActive(Long organizationId, Boolean isActive);

    Optional<OrganizationUser> findByOrganizationIdAndUserId(Long organizationId, Long userId);

    List<OrganizationUser> findAllByDepartmentId(Long departmentId);

    boolean existsByOrganizationIdAndUserId(Long organizationId, Long userId);
}
