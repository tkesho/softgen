package org.test.sotfgen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.test.sotfgen.dto.UserEntityDto;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    public ResponseEntity<Page<UserEntity>> getUsers(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection,
            @RequestParam(defaultValue = "id", required = false) String sortField
    ) {
        Sort sorter = Sort.by(sortDirection, sortField);
        return ResponseEntity.ok(userService.getUsers(PageRequest.of(pageNumber, pageSize, sorter)));
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntityDto user) {
        UserEntity createdUser = userService.createUser(user);
        var location = UriComponentsBuilder.fromPath("/users/" + createdUser.getId()).build().toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Integer id, @RequestBody UserEntityDto user) {
        UserEntity updatedUser = userService.updateUser(user, id);
        return ResponseEntity.accepted().body(updatedUser);
    }

    @PreAuthorize("hasAuthority('SER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
