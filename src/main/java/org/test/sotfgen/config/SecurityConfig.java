//package org.test.sotfgen.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.test.sotfgen.repository.UserRepository;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                var user = userRepository.findByUsername(username)
//                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//                return new SecUser(user);
//            }
//        };
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/bcrypt").permitAll()
//                        .requestMatchers("/swagger-ui.html").hasRole("ADMIN")
//                )
//                .httpBasic(withDefaults()) // Replacing deprecated `httpBasic()`
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//        return http.build();
//    }
//}