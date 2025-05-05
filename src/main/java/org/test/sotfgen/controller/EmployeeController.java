package org.test.sotfgen.controller;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.sotfgen.dto.EmployeeDto;
import org.test.sotfgen.dto.EmployeeSearchParams;
import org.test.sotfgen.entity.EmployeeEntity;
import org.test.sotfgen.service.interfaces.EmployeeService;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<EmployeeEntity>> getEmployee(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) @Max(100) Integer pageSize,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            EmployeeSearchParams params
    ) {
        Sort sorter = Sort.by(sortDirection, sortBy);
        return ResponseEntity.ok(employeeService.getEmployees(PageRequest.of(pageNumber, pageSize, sorter), params));
    }

    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(@RequestBody EmployeeDto employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }
}
