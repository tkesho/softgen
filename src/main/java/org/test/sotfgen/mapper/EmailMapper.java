package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.test.sotfgen.dto.EmailDto;
import org.test.sotfgen.entity.EmailEntity;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    @Mapping(target = "fromEmail", source = "fromEmail")
    @Mapping(target = "toEmail", source = "toEmail")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "body", source = "body")
    EmailEntity emailDtoToEmailEntity(EmailDto emailDto);

    @Mapping(target = "fromEmail", source = "fromEmail")
    @Mapping(target = "toEmail", source = "toEmail")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "body", source = "body")
    EmailDto emailEntityToEmailDto(EmailEntity emailEntity);
}
