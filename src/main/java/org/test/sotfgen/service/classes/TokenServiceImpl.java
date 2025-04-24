package org.test.sotfgen.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.AuthRequestDto;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.service.interfaces.TokenService;
import org.test.sotfgen.utils.TokenUtil;
import org.test.sotfgen.utils.UserUtil;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final AuthenticationManager authenticationManager;
    private final UserUtil userUtil;
    private final TokenUtil tokenUtil;
    private final EmailSenderService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String authenticateAndGenerateToken(AuthRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if(authentication == null) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream().map(
                GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return tokenUtil.generateAccessToken(username);
    }

    @Override
    public void logout() {
        UserEntity user = userUtil.getActingPrincipal();
        tokenUtil.deactivateToken(user.getUsername());
    }

    @Override
    public String refreshToken(String token) {
        return "";
    }

    @Override
    public void forgetPassword(String email) {
        try {
            userUtil.getUserByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("User with email " + email + " not found");
        }

        String resetLink = "https://localhost:8080/reset-password?token=" + tokenUtil.passwordResetToken(email);
        emailService.sendEmail(email, "Reset Password", "Click here: " + resetLink);
    }

    @Override
    public void resetPassword(String token) {
        UserEntity user = userUtil.getUserByEmail(tokenUtil.validatePasswordResetToken(token));

        String newPassword = userUtil.generateRandomPassword(12);
        user.setPassword(passwordEncoder.encode(newPassword));

        String message = String.format("Hello %s, Your new password has been set to: %s !", user.getUsername(), newPassword);
        emailService.sendEmail(user.getEmail(), "Password reset", message);
    }
}