package cl.privdata.organizationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.privdata.organizationService.model.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByOrganizationId(Long organizationId);

    List<Department> findAllByOrganizationIdAndIsActive(Long organizationId, Boolean isActive);

    boolean existsByOrganizationIdAndName(Long organizationId, String name);
}
