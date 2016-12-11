package de.kaufland.ksilence.repository;

import de.kaufland.ksilence.model.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

    Iterable<Department> findAll();

}
