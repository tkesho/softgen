package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.test.sotfgen.dto.PostDto;
import org.test.sotfgen.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "title", source = "title")
    @Mapping(target = "body", source = "body")
    PostEntity postDtoToPostEntity(PostDto postDto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "body", source = "body")
    PostDto postEntityToPostDto(PostEntity postEntity);
}
