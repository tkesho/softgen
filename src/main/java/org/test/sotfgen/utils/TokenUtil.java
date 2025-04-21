package org.test.sotfgen.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenUtil {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpirationMs;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationMs;

    // Generate Access Token
    public String generateAccessToken(String username, String authorities) {
        return Jwts.builder()
                .issuer("Softgen")
                .subject("JWT Access Token")
                .claim("username", username)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + accessTokenExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8))).compact();
    }

    // Generate Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject("JWT Refresh Token")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8))).compact();
    }
}