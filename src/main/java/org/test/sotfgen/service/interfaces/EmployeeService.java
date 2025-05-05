package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.EmployeeDto;
import org.test.sotfgen.dto.EmployeeSearchParams;
import org.test.sotfgen.entity.EmployeeEntity;

@Service
public interface EmployeeService {

    Page<EmployeeEntity> getEmployees(PageRequest of, EmployeeSearchParams params);

    EmployeeEntity createEmployee(EmployeeDto employee);

    EmployeeEntity updateEmployee(EmployeeDto employee, Integer employeeId);

    EmployeeEntity getEmployeeById(Integer id);
}
