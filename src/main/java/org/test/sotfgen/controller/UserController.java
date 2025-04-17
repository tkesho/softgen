package org.test.sotfgen.controller;

import jakarta.validation.constraints.Max;
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
import org.test.sotfgen.dto.Password;
import org.test.sotfgen.dto.UserDto;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.service.interfaces.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    public ResponseEntity<Page<UserEntity>> getUsers(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) @Max(100) Integer pageSize,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection,
            @RequestParam(defaultValue = "id", required = false) String sortField
    ) {
        Sort sorter = Sort.by(sortDirection, sortField);
        return ResponseEntity.ok(userService.getUsers(PageRequest.of(pageNumber, pageSize, sorter)));
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    public ResponseEntity<UserEntity> createUser(
            @RequestBody UserDto user
    ) {
        UserEntity createdUser = userService.createUser(user);
        var location = UriComponentsBuilder.fromPath("/users/" + createdUser.getId()).build().toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Void> resetPass(
            @AuthenticationPrincipal SecUser secUser
    ) {
        userService.resetPass(secUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Void> changePass(
            @AuthenticationPrincipal SecUser secUser,
            @RequestParam String oldPassword,
            @RequestBody Password newPassword
    ) {
        userService.changePass(secUser, oldPassword, newPassword);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/resetPassword/{username}")
    public ResponseEntity<Void> resetPassAdmin(
            @AuthenticationPrincipal SecUser secUser,
            String username
    ) {
        userService.resetPassAdmin(secUser, username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(
            @AuthenticationPrincipal SecUser secUser,
            @PathVariable Integer userId,
            @RequestBody UserDto userDto
    ) {
        UserEntity updatedUser = userService.updateEMail(secUser, userDto, userId);
        return ResponseEntity.accepted().body(updatedUser);
    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<Void> deactivateUser(
            @AuthenticationPrincipal SecUser secUser,
            @RequestBody Password password
    ) {
        userService.deactivateUser(secUser, password);
        return ResponseEntity.noContent().build();
    }
}
