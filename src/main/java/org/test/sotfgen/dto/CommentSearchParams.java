package org.test.sotfgen.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentSearchParams {
    private String body;
    private Integer postId;
    private Integer personId;
    private Boolean active;
}
