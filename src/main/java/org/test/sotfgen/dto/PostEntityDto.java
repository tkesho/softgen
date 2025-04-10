package org.test.sotfgen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostEntityDto {
    @NotNull
    @NotBlank
    private String title;
    private String body;
    @NotNull
    @NotEmpty
    private Integer userId;
    @NotNull
    @NotEmpty
    private Integer groupId;
    private Boolean hidden;
    private Set<Integer> fileIds;
}
