package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.test.sotfgen.dto.EmployeeDto;
import org.test.sotfgen.entity.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeEntity employeeDtoToEmployee(EmployeeDto employeeEntitydto);

    EmployeeDto employeeToEmployeeDto(EmployeeEntity employeeEntity);

    void update(EmployeeDto employeeDto, @MappingTarget EmployeeEntity personEntity);
}