package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.test.sotfgen.dto.GroupDto;
import org.test.sotfgen.entity.GroupEntity;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "privacy", source = "privacy")
    GroupDto groupEntityToGroupDto(GroupEntity groupDto);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "privacy", source = "privacy")
    GroupEntity groupDtoToGroupEntity(GroupDto groupEntity);
}
