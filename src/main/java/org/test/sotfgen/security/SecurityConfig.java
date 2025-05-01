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
import org.test.sotfgen.security.filter.AuthoritiesLoggingAfterFilter;
import org.test.sotfgen.security.filter.JWTTokenValidatorFilter;
import org.test.sotfgen.security.filter.RequestLoggingFilter;
import org.test.sotfgen.service.interfaces.LoggingService;
import org.test.sotfgen.utils.UserUtil;

@Configuration
@Profile("!prod")
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

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
    public JWTTokenValidatorFilter jwtTokenValidatorFilter(RedisTemplate<String,String> redisTemplate, UserUtil userUtil) {
        return new JWTTokenValidatorFilter(jwtSecretKey, jwtHeader, redisTemplate, this.userUtil);
    }

    @Bean
    //Filter
    public RequestLoggingFilter requestLoggingFilter(LoggingService loggingService) {
        return new RequestLoggingFilter(loggingService);
    }

    @Bean
    //Filter
    public AuthoritiesLoggingAfterFilter authoritiesLoggingAfterFilter() {
        return new AuthoritiesLoggingAfterFilter();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(authoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(jwtTokenValidatorFilter(redisTemplate, userUtil), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/request-password-reset", "/reset-password/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}