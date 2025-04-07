package org.test.sotfgen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.test.sotfgen.entity.GroupEntity;

@Setter
@Getter
public class GroupEntityDto {
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private Boolean active;
    @NotNull
    @NotBlank
    private Integer ownerId;
    @NotNull
    @NotBlank
    private GroupEntity.Privacy privacy;
}