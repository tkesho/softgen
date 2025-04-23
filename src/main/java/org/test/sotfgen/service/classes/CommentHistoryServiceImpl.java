package org.test.sotfgen.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.CommentHistorySearchParams;
import org.test.sotfgen.audit.CommentHistoryEntity;
import org.test.sotfgen.repository.CommentHistoryRepository;
import org.test.sotfgen.service.interfaces.CommentHistoryService;

@Service
@RequiredArgsConstructor

public class CommentHistoryServiceImpl implements CommentHistoryService {

    private final CommentHistoryRepository commentHistoryRepository;

    @Override
    public Page<CommentHistoryEntity> getAllCommentHistory(CommentHistorySearchParams params, Pageable pageable) {
        return commentHistoryRepository.findAll((root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (params.getPostId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("post_id"), params.getPostId()));
            }
            if (params.getCommentId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("comment_id"), params.getCommentId()));
            }
            if (params.getPersonId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("author_id"), params.getPersonId()));
            }
            if (params.getBody() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("body"), "%" + params.getBody() + "%"));
            }
            if (params.getHidden() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("hidden"), params.getHidden()));
            }
            return predicate;
        }, pageable);
    }
}
