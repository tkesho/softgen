package org.test.sotfgen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @PreAuthorize("true")
    @GetMapping("/bcrypt")
    public String bcrypt(@RequestParam String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @PreAuthorize("true")
    @GetMapping("/test")
    public ResponseEntity<String> test(Authentication authentication) {
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            System.out.println("Authority: " + auth.getAuthority());
        }
        return ResponseEntity.ok("Logged in as: " + authentication.getName());
    }
}
