package com.api.ecnomia_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artigos/**").permitAll()


                    //depois devem ser excluidos
                    .requestMatchers(HttpMethod.GET, "/api/usuarios/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/usuarios/**").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").permitAll()


                    .requestMatchers(HttpMethod.POST, "/api/artigos/**").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/artigos/**").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/artigos/**").permitAll()

                    .requestMatchers(HttpMethod.POST, "/api/categorias/**").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/categorias/**").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").permitAll()


                    .requestMatchers(HttpMethod.POST, "/api/foruns/**").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/foruns/**").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/foruns/**").permitAll()

                    .requestMatchers(HttpMethod.POST, "/api/quiz/**").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/quiz/**").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/quiz/**").permitAll()
                    //depois devem ser excluidos


                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/foruns/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/quiz/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
