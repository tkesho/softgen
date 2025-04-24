package org.test.sotfgen.service.classes;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.exceptions.CommentNotFoundException;
import org.test.sotfgen.exceptions.PersonNotFoundException;
import org.test.sotfgen.exceptions.UserDoesNotHasAuthority;
import org.test.sotfgen.dto.CommentDto;
import org.test.sotfgen.dto.CommentSearchParams;
import org.test.sotfgen.entity.CommentEntity;
import org.test.sotfgen.audit.CommentHistoryEntity;
import org.test.sotfgen.mapper.CommentMapper;
import org.test.sotfgen.repository.CommentHistoryRepository;
import org.test.sotfgen.repository.CommentRepository;
import org.test.sotfgen.repository.PersonRepository;
import org.test.sotfgen.security.SecUser;
import org.test.sotfgen.service.interfaces.CommentService;
import org.test.sotfgen.service.interfaces.PostService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentHistoryRepository commentHistoryRepository;
    private final PersonRepository personDetailService;
    private final PostService postService;

    @Override
    public Page<CommentEntity> getComments(CommentSearchParams params, Pageable pageable) {
        return commentRepository.findAll((root, criteriaQuery, cb) -> getPredicate(root, cb, params), pageable);
    }

    @Override
    public CommentEntity getComment(Integer commentId) {
        return getCommentById(commentId);
    }

    @Override
    public CommentEntity createComment(SecUser secUser, CommentDto commentDto, Integer postId, Integer commentId) {
        CommentEntity newComment = commentMapper.CommentDtoToCommentEntity(commentDto);
        newComment.setAuthor(personDetailService.findByUserId(secUser.getId()).orElseThrow(() -> new PersonNotFoundException("person with id " + secUser.getId() + " not found")));
        newComment.setPost(postService.getPostById(postId));
        newComment.setPost(postService.getPostById(postId));
        if(commentId != null) {
            newComment.setParent(getCommentById(commentId));
        }

        newComment = commentRepository.save(newComment);
        commentRepository.flush();
        CreateHistoryEntry(newComment);

        return newComment;
    }

    @Override
    public CommentEntity updateComment(SecUser secUser, Integer commentId, CommentDto comment) {
        CommentEntity existingComment = getCommentById(commentId);
        if(!existingComment.getAuthor().getUser().getId().equals(secUser.getId())) {
            throw new UserDoesNotHasAuthority("user is not the author of the comment with id " + commentId);
        }
        commentMapper.updateCommentEntityFromDto(comment, existingComment);
        existingComment = commentRepository.save(existingComment);
        commentRepository.flush();
        CreateHistoryEntry(existingComment);

        return existingComment;
    }

    @Override
    public void deleteComment(SecUser secUser, Integer commentId) {

    }

    @Override
    public CommentEntity getCommentById(Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("comment with id " + commentId + " not found"));
    }


    private CommentEntity updateCommentFields(CommentDto commentDto) {
        return null;
    }

    private Predicate getPredicate(Root<CommentEntity> root, CriteriaBuilder cb, CommentSearchParams params) {
        Predicate predicate = cb.conjunction();

        if (params.getPostId() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("post_id"), params.getPostId()));
        }
        if (params.getBody() != null && StringUtils.isNotBlank(params.getBody())) {
            predicate = cb.and(predicate, cb.like(root.get("body"), "%" + params.getBody() + "%"));
        }
        if (params.getActive() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("active"), params.getActive()));
        }
        if (params.getPersonId() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("personId"), params.getPersonId()));
        }

        return predicate;
    }

    private void CreateHistoryEntry(CommentEntity comment) {
        CommentHistoryEntity history = new CommentHistoryEntity();
        if (comment.getId() != null) {
            history.setCommentId(comment.getId());
        }
        if (comment.getBody() != null) {
            history.setBody(comment.getBody());
        }
        if (comment.getPost() != null && comment.getPost().getId() != null) {
            history.setPostId(comment.getPost().getId());
        }
        if (comment.getAuthor() != null && comment.getAuthor().getId() != null) {
            history.setAuthorId(comment.getAuthor().getId());
        }
        if (comment.getParent() != null && comment.getParent().getId() != null) {
            history.setParentId(comment.getParent().getId());
        }
        if (comment.getHidden() != null) {
            history.setHidden(comment.getHidden());
        }
        if (comment.getCreatedBy() != null) {
            history.setCreatedBy(comment.getCreatedBy());
        }
        if (comment.getLastModifiedBy() != null) {
            history.setLastModifiedBy(comment.getLastModifiedBy());
        }
        if (comment.getCreatedDate() != null) {
            history.setCreatedDate(comment.getCreatedDate());
        }
        if (comment.getLastModifiedDate() != null) {
            history.setLastModifiedDate(comment.getLastModifiedDate());
        }

        commentHistoryRepository.save(history);
    }
}
