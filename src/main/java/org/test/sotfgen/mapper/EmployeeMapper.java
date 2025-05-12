package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.test.sotfgen.dto.EmployeeDto;
import org.test.sotfgen.entity.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends Mappable<EmployeeEntity, EmployeeDto> {

}