package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.PostDto;
import org.test.sotfgen.dto.PostSearchParams;
import org.test.sotfgen.entity.PostEntity;

@Service
public interface PostService {
    Page<PostEntity> getPosts(PostSearchParams params, PageRequest of);

    PostEntity getPost(Integer postId);

    PostEntity createPost(Integer groupId, PostDto postDto);

    PostEntity updatePost(PostDto postDto, Integer postId);

    void deletePost(Integer postId);

    PostEntity getPostById(Integer postId);
}
