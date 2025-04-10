package org.test.sotfgen.controller;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.test.sotfgen.dto.PostEntityDto;
import org.test.sotfgen.dto.PostSearchParams;
import org.test.sotfgen.entity.PostEntity;
import org.test.sotfgen.service.PostService;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasAuthority('POST_READ')")
    @GetMapping
    public ResponseEntity<Page<PostEntity>> getPosts(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) @Max(100) Integer pageSize,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            PostSearchParams params) {
        Sort sorter = Sort.by(sortDirection, sortBy);
        return ResponseEntity.ok(postService.getPosts(params, PageRequest.of(pageNumber, pageSize, sorter)));
    }

    @PreAuthorize("hasAuthority('POST_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<PostEntity> getPost(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PreAuthorize("hasAuthority('POST_CREATE')")
    @PostMapping
    public ResponseEntity<PostEntity> createPost(
            @RequestBody PostEntityDto post) {
        PostEntity createdPost = postService.createPost(post);
        var location = UriComponentsBuilder.fromPath("/posts/{id}").buildAndExpand(createdPost.getId());
        return ResponseEntity.created(location.toUri()).body(createdPost);
    }

    @PreAuthorize("hasAuthority('POST_UPDATE')")
    @PutMapping({"/{postId}"})
    public ResponseEntity<PostEntity> updatePost(
            @PathVariable Integer postId,
            @RequestBody PostEntityDto post) {
        PostEntity updatedPost = postService.updatePost(post, postId);
        return ResponseEntity.accepted().body(updatedPost);
    }

    @PreAuthorize("hasAuthority('POST_DELETE')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
