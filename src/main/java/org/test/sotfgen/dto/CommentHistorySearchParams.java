package org.test.sotfgen.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentHistorySearchParams {
    private Integer postId;
    private Integer commentId;
    private Integer personId;
    private String body;
    private Boolean hidden = false;
}
