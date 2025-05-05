package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends BasicRepository<EmployeeEntity, Integer> {
}
