package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.test.sotfgen.dto.UserDto;
import org.test.sotfgen.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "email", source = "email")
    UserDto userToUserDto(UserEntity userEntity);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "email", source = "email")
    UserEntity userDtoToUser(UserDto userDto);
}
