package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.test.sotfgen.dto.PersonDetailDto;
import org.test.sotfgen.entity.PersonDetailEntity;

@Mapper(componentModel = "spring")
public interface PersonDetailMapper {

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "nationalId", source = "nationalId")
    @Mapping(target = "gender", source = "gender")
    PersonDetailEntity personDetailDtoToPersonDetail(PersonDetailDto personDetailDto);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "nationalId", source = "nationalId")
    @Mapping(target = "gender", source = "gender")
    PersonDetailDto personDetailToPersonDetailDto(PersonDetailDto personDetailDto);
}