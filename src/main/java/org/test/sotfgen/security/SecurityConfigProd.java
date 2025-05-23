package org.test.sotfgen.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.test.sotfgen.security.filter.JWTTokenValidatorFilter;
import org.test.sotfgen.utils.UserUtil;

import java.util.Collections;
import java.util.List;

@Configuration
@Profile("prod")
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigProd {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;
    @Value("${jwt.header}")
    private String jwtHeader;

    private final RedisTemplate<String,String> redisTemplate;
    private final UserUtil userUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        CustomAuthenticationProvider authenticationProvider =
                new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager provideManager = new ProviderManager(authenticationProvider);
        provideManager.setEraseCredentialsAfterAuthentication(false);
        return provideManager;
    }

    @Bean
    //Filter
    public JWTTokenValidatorFilter jwtTokenValidatorFilter(RedisTemplate<String,String> redis, UserUtil userUtil) {
        return new JWTTokenValidatorFilter(jwtSecretKey, jwtHeader, redis, userUtil);
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, UserUtil userUtil) throws Exception{
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // HTTPS request
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenValidatorFilter(redisTemplate, userUtil), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}