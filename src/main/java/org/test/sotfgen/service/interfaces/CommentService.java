package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.security.SecUser;
import org.test.sotfgen.dto.CommentDto;
import org.test.sotfgen.dto.CommentSearchParams;
import org.test.sotfgen.entity.CommentEntity;

@Service
public interface CommentService {
    Page<CommentEntity> getComments(CommentSearchParams params, Pageable pageable);

    CommentEntity getComment(Integer commentId);

    CommentEntity createComment(SecUser secUser, CommentDto comment, Integer postId, Integer commentId);

    CommentEntity updateComment(SecUser secUser, Integer commentId, CommentDto comment);

    void deleteComment(SecUser secUser, Integer commentId);

    CommentEntity getCommentById(Integer commentId);
}
