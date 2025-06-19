package com.app.restaurant_management.config.security;

import com.app.restaurant_management.commons.enums.RoleName;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
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
                                .requestMatchers(HttpMethod.POST, "/api/v1/foods/addFood").hasRole(String.valueOf(RoleName.ADMIN))
                                .requestMatchers("/api/v1/auth/**","/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                        ).exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntrypoint)
                        ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
