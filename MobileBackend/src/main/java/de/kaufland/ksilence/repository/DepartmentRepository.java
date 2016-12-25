package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.MobileDepartment;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<MobileDepartment, Long> {

    Iterable<MobileDepartment> findAll();

}
