package org.test.sotfgen.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.test.sotfgen.utils.UserUtil;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final String jwtSecretKey;
    private final String jwtHeader;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserUtil userUtil;

    public JWTTokenValidatorFilter(String jwtSecretKey, String jwtHeader, RedisTemplate<String, String> redisTemplate, UserUtil userUtil) {
        this.jwtSecretKey = jwtSecretKey;
        this.jwtHeader = jwtHeader;
        this.redisTemplate = redisTemplate;
        this.userUtil = userUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String jwt = request.getHeader(jwtHeader);
        if(jwt != null) {

            SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().verifyWith(secretKey)
                    .build().parseSignedClaims(jwt).getPayload();

            String username = String.valueOf(claims.get("username"));
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.addAll(userUtil.getUserByUsername(username).getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).toList());

            if(redisTemplate.opsForValue().get(username) != null) {

                try {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    throw new BadCredentialsException("Invalid JWT token");
                }

            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/login")
                || path.equals("/swagger-ui/index.html")
                || path.equals("/v3/api-docs")
                || path.equals("/swagger-resources")
                || path.equals("/webjars");
    }
}