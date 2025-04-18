package org.test.sotfgen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.GroupDto;
import org.test.sotfgen.dto.GroupSearchParams;
import org.test.sotfgen.entity.GroupEntity;

@Service
public interface GroupService {

    Page<GroupEntity> getGroups(GroupSearchParams params, Pageable pageable);

    GroupEntity getGroup(Integer id);

    GroupEntity createGroup(SecUser author, GroupDto group);

    GroupEntity updateGroup(SecUser author, GroupDto group, Integer groupId);

    void deletePerson(Integer id);

    void insertUserToGroup(Integer userId, Integer groupId);

    void deleteUserFromGroup(Integer userId, Integer groupId);

    GroupEntity getGroupById(Integer id);
}