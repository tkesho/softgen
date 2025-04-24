package org.test.sotfgen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.sotfgen.dto.AuthRequestDto;
import org.test.sotfgen.dto.AuthResponseDto;
import org.test.sotfgen.service.interfaces.TokenService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final TokenService tokenService;

    @Value("${jwt.header}")
    private String jwtHeader;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticate(
            @RequestBody AuthRequestDto authRequestDto
    ) {
        String jwtAccess = tokenService.authenticateAndGenerateToken(authRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(HttpStatus.OK.getReasonPhrase(), jwtAccess));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        tokenService.logout();
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/request-password-reset")
    ResponseEntity<String> forgetPassword(@RequestBody String email) {
        tokenService.forgetPassword(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @GetMapping("/reset-password")
    ResponseEntity<String> resetPassword(@RequestParam String token) {
        tokenService.resetPassword(token);
        return ResponseEntity.ok("Password reset successful");
    }
}