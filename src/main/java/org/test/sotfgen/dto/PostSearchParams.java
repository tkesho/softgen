package org.test.sotfgen.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostSearchParams {
    private String title;
    private String body;
    private Integer userId;
    private Integer groupId;
    private Boolean active;
}
