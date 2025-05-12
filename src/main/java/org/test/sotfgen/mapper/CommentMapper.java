package org.test.sotfgen.mapper;

import org.mapstruct.Mapper;
import org.test.sotfgen.dto.CommentDto;
import org.test.sotfgen.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper extends Mappable<CommentEntity, CommentDto> {
}
