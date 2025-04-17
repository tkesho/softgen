package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.PostDto;
import org.test.sotfgen.dto.PostSearchParams;
import org.test.sotfgen.entity.PostEntity;

@Service
public interface PostService {
    Page<PostEntity> getPosts(PostSearchParams params, PageRequest of);

    PostEntity getPost(Integer postId);

    PostEntity createPost(SecUser author, Integer groupId, PostDto postDto);

    PostEntity updatePost(SecUser userId, PostDto postDto, Integer postId);

    void deletePost(Integer postId);

    PostEntity getPostById(Integer postId);
}
