package org.test.sotfgen.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.AuthRequestDto;
import org.test.sotfgen.service.interfaces.TokenService;
import org.test.sotfgen.utils.TokenUtil;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenUtil tokenUtil;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpirationMs;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationMs;

    @Override
    public String authenticateAndGenerateToken(AuthRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if(authentication == null) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream().map(
                GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String jwt = tokenUtil.generateAccessToken(username, authorities);

        storeToken(jwt, accessTokenExpirationMs);

        return jwt;
    }

    @Override
    public void logout(String token) {
        redisTemplate.opsForValue().set(token, "not valid");
    }

    @Override
    public String refreshToken(String token) {
        return "";
    }

    private void storeToken(String token, Long expiration) {
        redisTemplate.opsForValue().set(token, "valid", expiration, TimeUnit.MILLISECONDS);
    }
}