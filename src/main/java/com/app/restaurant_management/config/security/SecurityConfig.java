package com.app.restaurant_management.config.security;

import com.app.restaurant_management.commons.enums.RoleName;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtEntrypoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtFilter, JwtAuthenticationEntryPoint jwtEntrypoint){
        this.jwtFilter = jwtFilter;
        this.jwtEntrypoint = jwtEntrypoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/menu/add").hasRole(String.valueOf(RoleName.ADMIN))
                                .requestMatchers(HttpMethod.POST, "/api/v1/foods/add", "/api/v1/foods/restore", "/api/v1/foods/softDelete").hasRole(String.valueOf(RoleName.ADMIN))
                                .requestMatchers(HttpMethod.PUT, "/api/v1/foods/update", "/api/v1/menu/update").hasRole(String.valueOf(RoleName.ADMIN))
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/foods/hardDelete", "/api/v1/menu/delete").hasRole(String.valueOf(RoleName.ADMIN))
                                .requestMatchers("/api/v1/auth/**","/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                        ).exceptionHandling(exception -> exception
                                .authenticationEntryPoint(jwtEntrypoint)
                                .accessDeniedHandler(accessDeniedHandler())
                        ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return ((request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(Map.of(
                            "status", 403,
                            "error", "Access Denied: You do not have permission to access this resource."
                    ))
            );
        });
    }
}
