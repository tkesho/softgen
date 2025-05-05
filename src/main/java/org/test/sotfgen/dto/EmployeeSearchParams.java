package org.test.sotfgen.dto;

import org.test.sotfgen.entity.EmployeeEntity;

public record EmployeeSearchParams(
        Integer salary,
        EmployeeEntity.EmploymentStatus employmentStatus,
        EmployeeEntity.EmploymentType employmentType,
        Integer personId,
        String position
) {
}
