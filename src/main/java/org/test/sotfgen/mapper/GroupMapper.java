package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.test.sotfgen.dto.GroupDto;
import org.test.sotfgen.entity.GroupEntity;

@Mapper(componentModel = "spring")
public interface GroupMapper extends Mappable<GroupEntity, GroupDto> {

}
