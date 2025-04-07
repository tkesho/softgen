package org.test.sotfgen.service;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.group.GroupNotFoundException;
import org.test.sotfgen.Exceptions.user.UserDoesNotHasAuthority;
import org.test.sotfgen.dto.GroupEntityDto;
import org.test.sotfgen.dto.GroupSearchParams;
import org.test.sotfgen.entity.GroupEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.GroupRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

    @Override
    public Page<GroupEntity> getGroups(GroupSearchParams params, Pageable pageable) {

        Page<GroupEntity> page = groupRepository.findAll((root, criteriaQuery, cb) -> getPredicate(params, root, cb), pageable);

        if (userHasAuthority("GROUP_READ_PRIVATE")) {
            return page;
        }

        List<GroupEntity> filtered = page.getContent().stream()
                .filter(group -> group.getPrivacy() == GroupEntity.Privacy.PUBLIC)
                .toList();
        return new PageImpl<>(filtered, pageable, page.getTotalElements());
    }

    @Override
    public GroupEntity getGroup(Integer id) {
        return getGroupById(id);
    }

    @Override
    public GroupEntity createGroup(GroupEntityDto group) {
        GroupEntity groupToCreate = new GroupEntity(group);
        UserEntity user = userService.getUserById(group.getOwnerId());
        groupToCreate.setOwner(user);
        return groupRepository.save(groupToCreate);
    }

    @Override
    public GroupEntity updateGroup(GroupEntityDto group, Integer id) {
        return groupRepository.save(updateGroupFields(group, id));
    }

    @Override
    public void deletePerson(Integer id) {
        GroupEntity groupToDelete = getGroupById(id);
        groupToDelete.setActive(false);
        groupRepository.save(groupToDelete);
    }

    private GroupEntity updateGroupFields(GroupEntityDto group, Integer id) {
        boolean hasGroupCreate = false;
        boolean flag = false;
        UserEntity newOwner = null;
        if(group.getOwnerId() != null) {
            flag = true;
            newOwner = userService.getUserById(group.getOwnerId());
    
            hasGroupCreate = newOwner.getRoles().stream()
                    .flatMap(role -> role.getAuthorities().stream())
                    .anyMatch(authority -> "GROUP_CREATE".equals(authority.getAuthority()));
        }

        if (hasGroupCreate == flag) {
            GroupEntity groupToUpdate = getGroupById(id);
            if (group.getName() != null) {
                groupToUpdate.setName(group.getName());
            }
            groupToUpdate.setDescription(group.getDescription());
            if(flag) {
                groupToUpdate.setOwner(newOwner);
            }
            if(group.getActive() != null) {
                groupToUpdate.setActive(group.getActive());
            }
            if (group.getActive() != null) {
                groupToUpdate.setPrivacy(group.getPrivacy());
            }
            return groupToUpdate;
        }

        throw new UserDoesNotHasAuthority("user with id " + group.getOwnerId() + " does not have GROUP_CREATE permission to own groups");
    }

    private GroupEntity getGroupById(Integer userId) {
        return groupRepository.findById(userId).orElseThrow(() -> new GroupNotFoundException("group with id  " + userId + " not found"));
    }

    private Predicate getPredicate(GroupSearchParams params, Root<GroupEntity> root, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        if (StringUtils.isNotBlank(params.getName())) {
            predicate = cb.and(predicate, cb.like(root.get("name"), "%" + params.getName() + "%"));
        }
        if (StringUtils.isNotBlank(params.getDescription())) {
            predicate = cb.and(predicate, cb.like(root.get("description"), "%" + params.getDescription() + "%"));
        }
        if (params.getOwnerId() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("owner_id"), params.getOwnerId()));
        }
        if (params.getActive() != null) {
            predicate = cb.and(predicate, cb.equal(root.get("active"), params.getActive()));
        }

        return predicate;
    }

    private static boolean userHasAuthority(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }
}
