package org.test.sotfgen.dto;

import lombok.Getter;
import lombok.Setter;
import org.test.sotfgen.entity.GroupEntity;

@Setter
@Getter
public class GroupSearchParams {
    private String name;
    private String description;
    private Boolean active = true;
    private GroupEntity.Privacy privacy;
    private Integer ownerId;
}
