package org.test.sotfgen.service;


import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.post.PostNotFoundException;
import org.test.sotfgen.Exceptions.user.UserDoesNotHasAuthority;
import org.test.sotfgen.dto.PostEntityDto;
import org.test.sotfgen.dto.PostSearchParams;
import org.test.sotfgen.entity.FileEntity;
import org.test.sotfgen.entity.GroupEntity;
import org.test.sotfgen.entity.PostEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.PostRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final GroupService groupService;
    private final FileService fileService;

    @Override
    public Page<PostEntity> getPosts(PostSearchParams params, PageRequest of) {
        return postRepository.findAll((root, criteriaQuery, cb) -> getPredicate(root, cb, params), of);
    }

    @Override
    public PostEntity getPost(Integer postId) {
        return getPostById(postId);
    }

    @Override
    @Transactional
    public PostEntity createPost(PostEntityDto post) {
        UserEntity author = userService.getUserById(post.getUserId());
        GroupEntity group = groupService.getGroupById(post.getGroupId());

        if (userService.userHasAuthority(author, "POST_CREATE")) {
            PostEntity postToCreate = new PostEntity(post);
            postToCreate.setUser(author);
            postToCreate.setGroup(group);
            Set<FileEntity> files = new HashSet<>();
            if(post.getFileIds() != null && !post.getFileIds().isEmpty()) {
                post.getFileIds().forEach(file -> files.add(fileService.getFileById(file)));
            }
            postToCreate.setFiles(files);
            return postRepository.save(postToCreate);
        } else {
            throw new UserDoesNotHasAuthority("user does not have authority to create a post");
        }
    }

    @Override
    @Transactional
    public PostEntity updatePost(PostEntityDto post, Integer postId) {
        PostEntity existingPost = getPostById(postId);

        if (StringUtils.isNotBlank(post.getTitle())) {
            existingPost.setTitle(post.getTitle());
        }
        if (StringUtils.isNotBlank(post.getBody())) {
            existingPost.setBody(post.getBody());
        }
        if (post.getHidden() != null) {
            existingPost.setHidden(post.getHidden());
        }
        if (post.getFileIds() != null && !post.getFileIds().isEmpty()) {
            Set<FileEntity> files = new HashSet<>();
            post.getFileIds().forEach(postFileId -> files.add(fileService.getFileById(postFileId)));
            existingPost.setFiles(files);
        }

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(Integer postId) {
        PostEntity ToDelete = getPostById(postId);
        postRepository.delete(ToDelete);
    }

    @Override
    public PostEntity getPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post with id " + postId + " not found"));
    }

    private Predicate getPredicate(Root<PostEntity> root, CriteriaBuilder cb, PostSearchParams params) {
        Predicate predicate = cb.conjunction();

        if (StringUtils.isNotBlank(params.getTitle())) {
            predicate = cb.and(predicate, cb.like(root.get("title"), "%" + params.getTitle() + "%"));
        }
        if (StringUtils.isNotBlank(params.getBody())) {
            predicate = cb.and(predicate, cb.like(root.get("body"), "%" + params.getBody() + "%"));
        }
        if (params.getUserId() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("user_id"), params.getUserId()));
        }
        if (params.getGroupId() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("group_id"), params.getGroupId()));
        }
        if (params.getActive() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("active"), params.getActive()));
        }

        return predicate;
    }
}
