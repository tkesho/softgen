package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.CommentHistorySearchParams;
import org.test.sotfgen.entity.CommentHistoryEntity;

@Service
public interface CommentHistoryService {

    Page<CommentHistoryEntity> getAllCommentHistory(CommentHistorySearchParams params, Pageable pageable);
}
