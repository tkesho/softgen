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
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.PostDto;
import org.test.sotfgen.dto.PostSearchParams;
import org.test.sotfgen.entity.FileEntity;
import org.test.sotfgen.entity.GroupEntity;
import org.test.sotfgen.entity.PostEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.mapper.PostMapper;
import org.test.sotfgen.repository.PostRepository;
import org.test.sotfgen.utils.UserServiceUtil;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserServiceUtil userServiceUtil;
    private final GroupService groupService;
    private final FileService fileService;
    private final PostMapper postMapper;

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
    public PostEntity createPost(SecUser secUser, Integer groupId, PostDto postDto) {
        UserEntity author = userServiceUtil.getUserById(secUser.getId());
        GroupEntity group = groupService.getGroupById(postDto.getGroupId());

            PostEntity postToCreate = postMapper.postDtoToPostEntity(postDto);
            postToCreate.setUser(author);
            postToCreate.setGroup(group);
            Set<FileEntity> files = new HashSet<>();
            if(postDto.getFileIds() != null && !postDto.getFileIds().isEmpty()) {
                postDto.getFileIds().forEach(file -> files.add(fileService.getFileById(file)));
            }
            postToCreate.setFiles(files);
            return postRepository.save(postToCreate);
    }

    @Override
    @Transactional
    public PostEntity updatePost(SecUser userId, PostDto post, Integer postId) {

        PostEntity postToUpdate = getPostById(postId);


        return postRepository.save(postToUpdate);
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

    private PostEntity updatePostFields(PostDto postDto, Integer postInteger) {
        PostEntity newPost = null;

        if(postDto.getOwnerId() != null && !postDto.getOwnerId().equals(getPostById(postInteger).getUser().getId())) {
            if(!userServiceUtil.userHasAuthority(postDto.getOwnerId(), "POST_CREATE")) {
                throw new UserDoesNotHasAuthority("user with id " + postDto.getOwnerId() + " does not have POST_CREATE permission to own posts");
            }
        }

        if (postDto.getGroupId() != null && !postDto.getGroupId().equals(getPostById(postInteger).getGroup().getId())) {
            if(!userServiceUtil.userHasRole(postDto.getGroupId(), "ROLE_ADMIN")) {
                throw new UserDoesNotHasAuthority("user with id " + postDto.getOwnerId() + " does not have ADMIN role change own posts");
            }
        } else {
            throw new UserDoesNotHasAuthority("user with id " + postDto.getOwnerId() + " does not have POST_CREATE permission to own posts");
        }

        if (postDto.getTitle() != null && !postDto.getTitle().isEmpty()) {
            newPost.setTitle(postDto.getTitle());
        }
        if (postDto.getBody() != null && StringUtils.isNotBlank(postDto.getBody())) {
            newPost.setBody(postDto.getBody());
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
}
