package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    List<Department> findAllByOrganizationId(UUID organizationId);

    List<Department> findAllByOrganizationIdAndIsActive(UUID organizationId, Boolean isActive);

    boolean existsByOrganizationIdAndName(UUID organizationId, String name);
}
