package cl.privdata.organizationService.repository;

import cl.privdata.organizationService.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    List<Person> findAllByOrganizationId(UUID organizationId);

    List<Person> findAllByOrganizationIdAndIsActive(UUID organizationId, Boolean isActive);

    List<Person> findAllByDepartmentId(UUID departmentId);

    boolean existsByOrganizationIdAndRut(UUID organizationId, String rut);

    boolean existsByOrganizationIdAndEmail(UUID organizationId, String email);
}
