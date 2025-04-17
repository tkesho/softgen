package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.test.sotfgen.dto.CommentDto;
import org.test.sotfgen.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "body", target = "body")
    CommentDto CommentEntityToCommentDto(CommentEntity commentEntity);

    @Mapping(source = "body", target = "body")
    CommentEntity CommentDtoToCommentEntity(CommentDto commentDto);

    void updateCommentEntityFromDto(CommentDto commentDto, @MappingTarget CommentEntity commentEntity);
}
