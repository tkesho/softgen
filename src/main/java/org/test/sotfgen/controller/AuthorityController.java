package org.test.sotfgen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.sotfgen.service.interfaces.AuthorityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authority")
public class AuthorityController {

    private final AuthorityService authorityService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{userId}/assign/{authorityId}")
    public void assignToUser(
            @PathVariable Integer userId,
            @PathVariable Integer authorityId
    ) {
        authorityService.assignToUser(userId, authorityId);
    }
}
