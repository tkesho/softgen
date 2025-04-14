package org.test.sotfgen.dto;

import lombok.Getter;
import lombok.Setter;
import org.test.sotfgen.entity.EmployeeEntity;

@Setter
@Getter
public class EmployeeDto {
    private Integer id;
    private Integer personId;
    private String position;
    private Integer salary;
    private EmployeeEntity.EmploymentType employmentType;
    private EmployeeEntity.EmploymentStatus employmentStatus;
}
