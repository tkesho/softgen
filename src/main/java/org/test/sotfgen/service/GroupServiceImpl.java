package org.test.sotfgen.service;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.group.GroupNotFoundException;
import org.test.sotfgen.Exceptions.group.MemberAndGroupRelationException;
import org.test.sotfgen.Exceptions.user.UserDoesNotHasAuthority;
import org.test.sotfgen.dto.GroupEntityDto;
import org.test.sotfgen.dto.GroupSearchParams;
import org.test.sotfgen.entity.GroupEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.GroupRepository;
import org.test.sotfgen.utils.UserServiceUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

    @Override
    public Page<GroupEntity> getGroups(GroupSearchParams params, Pageable pageable) {

        Page<GroupEntity> page = groupRepository.findAll((root, criteriaQuery, cb) -> getPredicate(params, root, cb), pageable);

        if (UserServiceUtil.activeUserHasAuthority("GROUP_READ_PRIVATE")) {
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
    @Transactional
    public GroupEntity createGroup(GroupEntityDto group) {
        GroupEntity groupToCreate = new GroupEntity(group);
        UserEntity user = userService.getUserById(group.getOwnerId());
        groupToCreate.setOwner(user);
        return groupRepository.save(groupToCreate);
    }

    @Override
    @Transactional
    public GroupEntity updateGroup(GroupEntityDto group, Integer id) {
        return groupRepository.save(updateGroupFields(group, id));
    }

    @Override
    @Transactional
    public void deletePerson(Integer id) {
        GroupEntity groupToDelete = getGroupById(id);
        groupToDelete.setActive(false);
        groupRepository.save(groupToDelete);
    }

    @Override
    @Transactional
    public void insertUserToGroup(Integer userId, Integer groupId) {
        UserEntity member = userService.getUserById(userId);
        GroupEntity group = getGroupById(groupId);

        if (group.getMembers().contains(member)) {
            throw new MemberAndGroupRelationException("user with id " + userId + " already belongs to group with id " + groupId);
        }

        group.getMembers().add(member);
        member.getGroups().add(group);

        groupRepository.save(group);
    }

    @Override
    public void deleteUserFromGroup(Integer userId, Integer groupId) {
        UserEntity member = userService.getUserById(userId);
        GroupEntity group = getGroupById(groupId);

        if (!group.getMembers().contains(member)) {
            throw new MemberAndGroupRelationException("user with id " + userId + " does not belong to group with id " + groupId);
        }

        group.getMembers().remove(member);
        member.getGroups().remove(group);

        groupRepository.save(group);
    }

    private GroupEntity updateGroupFields(GroupEntityDto group, Integer id) {
        boolean hasGroupCreate = false;
        boolean flag = false;
        UserEntity newOwner = null;
        if(group.getOwnerId() != null) {
            flag = true;
            newOwner = userService.getUserById(group.getOwnerId());
    
            hasGroupCreate = userService.userHasAuthority(newOwner, "GROUP_CREATE");
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

    public  GroupEntity getGroupById(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("group with id  " + id + " not found"));
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
}
