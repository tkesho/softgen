package org.test.sotfgen.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenUtil {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpirationMs;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationMs;

    private final RedisTemplate<String, String> redisTemplate;

    // Generate Access Token
    public String generateAccessToken(String username) {
        String jwtToken = Jwts.builder()
                .issuer("Softgen")
                .subject("JWT Access Token")
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + accessTokenExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8))).compact();

        redisTemplate.opsForValue().set(username, jwtToken, accessTokenExpirationMs, TimeUnit.MILLISECONDS);

        return jwtToken;
    }

    // deactivate token
    public void deactivateToken(String username) {
        redisTemplate.delete(username);
    }

    // Generate Refresh Token
    public String generateRefreshToken(String username) {
        String jwtToken = Jwts.builder()
                .subject("JWT Refresh Token")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8))).compact();

        redisTemplate.opsForValue().set(username, jwtToken, refreshTokenExpirationMs, TimeUnit.MILLISECONDS);

        return jwtToken;
    }

    public String passwordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        String redisKey = "reset:" + token;

        redisTemplate.opsForValue().set(redisKey, email, Duration.ofMinutes(30));
        return token;
    }

    public String validatePasswordResetToken(String token) {
        String redisKey = "reset:" + token;
        String email = redisTemplate.opsForValue().get(redisKey);
        redisTemplate.delete(redisKey);
        return email;
    }
}