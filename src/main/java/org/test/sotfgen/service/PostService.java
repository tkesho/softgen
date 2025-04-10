package org.test.sotfgen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.PostEntityDto;
import org.test.sotfgen.dto.PostSearchParams;
import org.test.sotfgen.entity.PostEntity;

@Service
public interface PostService {
    Page<PostEntity> getPosts(PostSearchParams params, PageRequest of);

    PostEntity getPost(Integer id);

    PostEntity createPost(PostEntityDto post);

    PostEntity updatePost(PostEntityDto post, Integer postId);

    void deletePost(Integer postId);


    PostEntity getPostById(Integer postId);
}
