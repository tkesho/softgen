package org.test.sotfgen.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeamSearchParams {
    private String name;
    private String description;
    private Integer managerId;
}
