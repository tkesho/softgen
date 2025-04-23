package org.test.sotfgen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.test.sotfgen.dto.CommentHistorySearchParams;
import org.test.sotfgen.audit.CommentHistoryEntity;
import org.test.sotfgen.service.interfaces.CommentHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commentHistory")
public class CommentHistoryController {

    private final CommentHistoryService commentHistoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<CommentHistoryEntity> getCommentAllHistory(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            CommentHistorySearchParams params
    ) {
        Sort sort = Sort.by(sortDirection, sortBy);
        return commentHistoryService.getAllCommentHistory(params, PageRequest.of(page, size, sort));
    }
}
