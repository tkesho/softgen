package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.test.sotfgen.dto.PersonDto;
import org.test.sotfgen.entity.PersonEntity;

@Mapper(componentModel = "spring")
public interface PersonMapper extends Mappable<PersonEntity, PersonDto> {
}