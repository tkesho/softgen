package org.test.sotfgen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.GroupDto;
import org.test.sotfgen.dto.GroupSearchParams;
import org.test.sotfgen.entity.GroupEntity;
import org.test.sotfgen.service.interfaces.GroupService;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PreAuthorize("hasAuthority('GROUP_READ_PUBLIC') or hasAuthority('GROUP_READ_PRIVATE')")
    @GetMapping
    public ResponseEntity<Page<GroupEntity>> getGroups(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            GroupSearchParams params
    ) {
        Sort sorter = Sort.by(sortDirection, sortField);
        return ResponseEntity.ok(groupService.getGroups(params, PageRequest.of(pageNumber, pageSize, sorter)));
    }

    @PreAuthorize("hasAuthority('GROUP_READ_PUBLIC') or hasAuthority('GROUP_READ_PRIVATE')")
    @GetMapping("/{id}")
    public ResponseEntity<GroupEntity> getGroup(@PathVariable Integer id) {
        return ResponseEntity.ok(groupService.getGroup(id));
    }

    @PreAuthorize("hasAuthority('GROUP_CREATE')")
    @PostMapping
    public ResponseEntity<GroupEntity> createGroup(@AuthenticationPrincipal SecUser secUser, @RequestBody GroupDto group) {
        GroupEntity createdGroup = groupService.createGroup(secUser, group);
        var location = UriComponentsBuilder.fromPath("/groups/" + createdGroup.getId()).build().toUri();
        return ResponseEntity.created(location).body(createdGroup);
    }

    @PreAuthorize("hasAuthority('GROUP_UPDATE')")
    @PutMapping("/{groupId}")
    public ResponseEntity<GroupEntity> updateGroup (
            @AuthenticationPrincipal SecUser secUser,
            @RequestBody GroupDto group,
            @PathVariable Integer groupId) {
        GroupEntity updatedGroup = groupService.updateGroup(secUser, group, groupId);
        return ResponseEntity.accepted().body(updatedGroup);
    }

    @PreAuthorize("hasAuthority('GROUP_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        groupService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('TEAM_LEADER')")
    @PostMapping("/{userId}/insert/{groupId}")
    public ResponseEntity<?> insertUserToGroup(@PathVariable Integer userId, @PathVariable Integer groupId) {
        groupService.insertUserToGroup(userId, groupId);
        return ResponseEntity.noContent().build ();
    }

    @PreAuthorize("hasRole('TEAM_LEADER')")
    @DeleteMapping("/{userId}/remove/{groupId}")
    public ResponseEntity<?> deleteUserFromGroup(@PathVariable Integer userId, @PathVariable Integer groupId) {
        groupService.deleteUserFromGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
}