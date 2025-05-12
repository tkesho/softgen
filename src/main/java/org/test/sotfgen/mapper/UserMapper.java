package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.test.sotfgen.dto.UserDto;
import org.test.sotfgen.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<UserEntity, UserDto> {
}
