package org.test.sotfgen.controller;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.CommentDto;
import org.test.sotfgen.dto.CommentSearchParams;
import org.test.sotfgen.entity.CommentEntity;
import org.test.sotfgen.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    //@PreAuthorize("hasAuthority('COMMENT_READ')")
    @GetMapping
    public Page<CommentEntity> getComments(
            @RequestParam (required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") @Max(100) Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            CommentSearchParams params
    ) {
        Sort sorter = Sort.by(sortDirection, sortBy);
        return commentService.getComments(params, PageRequest.of(page, size, sorter));
    }

    //@PreAuthorize("hasAuthority('COMMENT_READ')")
    @GetMapping("/{id}")
    public CommentEntity getComment(@PathVariable Integer id) {
        return commentService.getComment(id);
    }

    //@PreAuthorize("hasAuthority('COMMENT_CREATE')")
    @PostMapping("/{postId}/{commentId}")
    public CommentEntity createComment(
            @AuthenticationPrincipal SecUser secUser,
            @PathVariable Integer postId,
            @PathVariable Integer commentId,
            @RequestBody CommentDto comment
    ) {
        return commentService.createComment(secUser, comment, postId, commentId);
    }

    @PostMapping("/{postId}")
    public CommentEntity createCommentWithoutParentComment(
            @AuthenticationPrincipal SecUser secUser,
            @PathVariable Integer postId,
            @RequestBody CommentDto comment
    ) {
        return commentService.createComment(secUser, comment, postId, null);
    }

    @PutMapping("/{commentId}")
    public CommentEntity updateComment(
            @AuthenticationPrincipal SecUser secUser,
            @PathVariable Integer commentId,
            @RequestBody CommentDto comment
    ) {
        return commentService.updateComment(secUser, commentId, comment);
    }
}