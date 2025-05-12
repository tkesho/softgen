package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.test.sotfgen.dto.PostDto;
import org.test.sotfgen.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostMapper extends Mappable<PostEntity, PostDto> {
}
