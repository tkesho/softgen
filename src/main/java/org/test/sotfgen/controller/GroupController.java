package org.test.sotfgen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.test.sotfgen.dto.GroupEntityDto;
import org.test.sotfgen.dto.GroupSearchParams;
import org.test.sotfgen.entity.GroupEntity;
import org.test.sotfgen.service.GroupService;

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
    public ResponseEntity<GroupEntity> createGroup(@RequestBody GroupEntityDto group) {
        GroupEntity createdGroup = groupService.createGroup(group);
        var location = UriComponentsBuilder.fromPath("/groups/" + createdGroup.getId()).build().toUri();
        return ResponseEntity.created(location).body(createdGroup);
    }

    @PreAuthorize("hasAuthority('GROUP_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<GroupEntity> updateGroup (@PathVariable Integer id, @RequestBody GroupEntityDto group) {
        GroupEntity updatedGroup = groupService.updateGroup(group, id);
        return ResponseEntity.accepted().body(updatedGroup);
    }

    @PreAuthorize("hasAuthority('GROUP_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        groupService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}