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
import org.test.sotfgen.audit.PostHistoryEntity;
import org.test.sotfgen.dto.PostHistorySearchParams;
import org.test.sotfgen.service.interfaces.PostHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/postHistory")
public class PostHistoryController {

    private final PostHistoryService postHistoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<PostHistoryEntity> getPostAllHistory(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            PostHistorySearchParams params
    ) {
        Sort sort = Sort.by(sortDirection, sortBy);
        return postHistoryService.getAllPostHistory(params, PageRequest.of(page, size, sort));
    }
}
