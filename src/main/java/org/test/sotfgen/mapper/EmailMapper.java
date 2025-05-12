package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.test.sotfgen.dto.EmailDto;
import org.test.sotfgen.entity.EmailEntity;

@Mapper(componentModel = "spring")
public interface EmailMapper extends Mappable<EmailEntity, EmailDto> {

}
