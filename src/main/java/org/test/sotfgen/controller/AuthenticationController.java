package org.test.sotfgen.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.sotfgen.dto.AuthRequestDto;
import org.test.sotfgen.dto.AuthResponseDto;
import org.test.sotfgen.service.interfaces.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping()
@Slf4j
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
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String jwtToken = request.getHeader(jwtHeader);

        tokenService.logout(jwtToken);
        return ResponseEntity.ok("Logout successful");
    }
}
