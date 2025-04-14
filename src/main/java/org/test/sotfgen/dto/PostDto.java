package org.test.sotfgen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostDto {
    @NotNull
    @NotBlank
    private String title;
    private Integer ownerId;
    private Integer groupId;
    private String body;
    private Boolean hidden;
    private Set<Integer> fileIds;
}
