package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.test.sotfgen.dto.EmployeeDto;
import org.test.sotfgen.entity.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "employmentType", source = "employmentType")
    @Mapping(target = "employmentStatus ", source = "employmentStatus")
    EmployeeEntity employeeDtoToEmployee(EmployeeEntity employeeEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "employmentType", source = "employmentType")
    @Mapping(target = "employmentStatus", source = "employmentStatus")
    EmployeeDto employeeToEmployeeDto(EmployeeEntity employeeEntity);
}