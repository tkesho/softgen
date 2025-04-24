package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.test.sotfgen.dto.PersonDto;
import org.test.sotfgen.entity.PersonEntity;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "nationalId", source = "nationalId")
    @Mapping(target = "gender", source = "gender")
    PersonEntity personDetailDtoToPersonDetail(PersonDto personDto);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "nationalId", source = "nationalId")
    @Mapping(target = "gender", source = "gender")
    PersonDto personDetailToPersonDetailDto(PersonEntity personEntity);

    void updatePersonFromDto(PersonDto personDto, @MappingTarget PersonEntity personEntity);
}