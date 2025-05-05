package org.test.sotfgen.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.EmployeeDto;
import org.test.sotfgen.dto.EmployeeSearchParams;
import org.test.sotfgen.entity.EmployeeEntity;
import org.test.sotfgen.exceptions.EmployeeNotFoundException;
import org.test.sotfgen.mapper.EmployeeMapper;
import org.test.sotfgen.repository.EmployeeRepository;
import org.test.sotfgen.service.interfaces.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Page<EmployeeEntity> getEmployees(PageRequest of, EmployeeSearchParams params) {
        return employeeRepository.findAll((root, query, cb) -> getPredicate(root, cb, params), of);
    }

    @Override
    public EmployeeEntity createEmployee(EmployeeDto employee) {
        EmployeeEntity entityToSave = employeeMapper.employeeDtoToEmployee(employee);
        return employeeRepository.save(entityToSave);
    }

    @Override
    public EmployeeEntity updateEmployee(EmployeeDto employee, Integer employeeId) {
        EmployeeEntity entityToUpdate = getEmployeeById(employeeId);
        employeeMapper.update(employee, entityToUpdate);

        return employeeRepository.save(entityToUpdate);
    }

    @Override
    public EmployeeEntity getEmployeeById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }

    private Predicate getPredicate(Root<EmployeeEntity> root, CriteriaBuilder cb, EmployeeSearchParams params) {
        Predicate predicate = cb.conjunction();
        if (StringUtils.isNotBlank(params.position())) {
            predicate = cb.and(predicate, cb.like(root.get("name"), "%" + params.position() + "%"));
        }
        if (params.employmentType() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("employmentType"), params.employmentType()));
        }
        if (params.employmentStatus() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("employmentStatus"), params.employmentStatus()));
        }
        if(params.salary() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("salary"), params.salary()));
        }
        if(params.personId() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("person"), params.personId()));
        }
        return predicate;
    }
}
