package com.ecommerce.api.eshopper.config;

import com.ecommerce.api.eshopper.auth.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .dispatcherTypeMatchers(DispatcherType.FORWARD,DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.ASYNC).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/role/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/category/**").permitAll()
                        .requestMatchers("/product/**").permitAll()
                        .requestMatchers("/author/**").permitAll()
                        .requestMatchers("/greeting/**").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
