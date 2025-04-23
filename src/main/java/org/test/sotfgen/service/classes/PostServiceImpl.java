package org.test.sotfgen.service.classes;


import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.test.sotfgen.audit.PostHistoryEntity;
import org.test.sotfgen.exceptions.PostNotFoundException;
import org.test.sotfgen.exceptions.UserDoesNotHasAuthority;
import org.test.sotfgen.dto.PostDto;
import org.test.sotfgen.dto.PostSearchParams;
import org.test.sotfgen.entity.*;
import org.test.sotfgen.mapper.PostMapper;
import org.test.sotfgen.repository.PostHistoryRepository;
import org.test.sotfgen.repository.PostRepository;
import org.test.sotfgen.service.interfaces.FileService;
import org.test.sotfgen.service.interfaces.GroupService;
import org.test.sotfgen.service.interfaces.PostService;
import org.test.sotfgen.utils.UserUtil;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserUtil userUtil;
    private final GroupService groupService;
    private final FileService fileService;
    private final PostMapper postMapper;
    private final PostHistoryRepository postHistoryRepository;

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
    public PostEntity createPost(Integer groupId, PostDto postDto) {
        UserEntity secUser = userUtil.getActingPrincipal();
        UserEntity author = userUtil.getUserById(secUser.getId());
        GroupEntity group = groupService.getGroupById(groupId);

            PostEntity postToCreate = postMapper.postDtoToPostEntity(postDto);
            postToCreate.setUser(author);
            postToCreate.setGroup(group);
            Set<FileEntity> files = new HashSet<>();
            if(postDto.getFileIds() != null && !postDto.getFileIds().isEmpty()) {
                postDto.getFileIds().forEach(file -> files.add(fileService.getFileById(file)));
            }
            postToCreate.setFiles(files);
            postRepository.save(postToCreate);
            postRepository.flush();
            createHistoryEntry(postToCreate);
            return postToCreate;
    }

    @Override
    @Transactional
    public PostEntity updatePost(PostDto post, Integer postId) {

        UserEntity secUser = userUtil.getActingPrincipal();

        PostEntity postToUpdate = updatePostFields(post, postId);
        postToUpdate = postRepository.save(postToUpdate);

        createHistoryEntry(postToUpdate);

        return postToUpdate;
    }

    @Override
    public void deletePost(Integer postId) {
        PostEntity ToDelete = getPostById(postId);
        postRepository.delete(ToDelete);
        postRepository.flush();
        createHistoryEntry(ToDelete);
    }

    @Override
    public PostEntity getPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post with id " + postId + " not found"));
    }

    private PostEntity updatePostFields(PostDto postDto, Integer postId) {
        if(postDto.getOwnerId() != null && !postDto.getOwnerId().equals(getPostById(postId).getUser().getId())) {
            if(!userUtil.userHasAuthority(postDto.getOwnerId(), "POST_CREATE")) {
                throw new UserDoesNotHasAuthority("user with id " + postDto.getOwnerId() + " does not have POST_CREATE permission to own posts");
            }
        }

        if (postDto.getGroupId() != null && !postDto.getGroupId().equals(getPostById(postId).getGroup().getId())) {
            if(!userUtil.userHasRole(postDto.getGroupId(), "ROLE_ADMIN")) {
                throw new UserDoesNotHasAuthority("user with id " + postDto.getOwnerId() + " does not have ADMIN role to change location of the post");
            }
            if(postDto.getGroupId() != null) {
                throw new UserDoesNotHasAuthority("user with id " + postDto.getOwnerId() + " does not have POST_CREATE permission to own posts");
            }
        }

        PostEntity newPost = getPostById(postId);

        if (postDto.getTitle() != null) {
            newPost.setTitle(postDto.getTitle());
        }
        if (postDto.getBody() != null) {
            newPost.setBody(postDto.getBody());
        }
        if (postDto.getOwnerId() != null) {
            newPost.setUser(userUtil.getUserById(postDto.getOwnerId()));
        }
        if (postDto.getGroupId() != null) {
            newPost.setGroup(groupService.getGroupById(postDto.getGroupId()));
        }
        if (postDto.getHidden() != null) {
            newPost.setHidden(postDto.getHidden());
        }
        if (postDto.getFileIds() != null && !postDto.getFileIds().isEmpty()) {
            Set<FileEntity> files = new HashSet<>();
            postDto.getFileIds().forEach(postFileId -> files.add(fileService.getFileById(postFileId)));
            newPost.setFiles(files);
        }

        return newPost;
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

    private void createHistoryEntry(PostEntity post) {
        PostHistoryEntity history = new PostHistoryEntity();

        if(post.getHidden() != null) {
            history.setHidden(post.getHidden());
        }

        if (post.getTitle() != null) {
            history.setTitle(post.getTitle());
        }
        if (post.getBody() != null) {
            history.setBody(post.getBody());
        }
        if (post.getUser() != null && post.getUser().getId() != null) {
            history.setOwnerId(post.getUser().getId());
        }
        if (post.getGroup() != null && post.getGroup().getId() != null) {
            history.setGroupId(post.getGroup().getId());
        }
        if (post.getId() != null) {
            history.setPostId(post.getId());
        }
        if (post.getCreatedBy() != null) {
            history.setCreatedBy(post.getCreatedBy());
        }
        if (post.getLastModifiedBy() != null) {
            history.setLastModifiedBy(post.getLastModifiedBy());
        }
        if (post.getCreatedDate() != null) {
            history.setCreatedDate(post.getCreatedDate());
        }
        if (post.getLastModifiedDate() != null) {
            history.setLastModifiedDate(post.getLastModifiedDate());
        }

        postHistoryRepository.save(history);
    }
}
