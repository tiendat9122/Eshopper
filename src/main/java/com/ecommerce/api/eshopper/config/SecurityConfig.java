package com.ecommerce.api.eshopper.config;

import com.ecommerce.api.eshopper.auth.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.Calendar;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
//                        .dispatcherTypeMatchers(DispatcherType.FORWARD,DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.ASYNC).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/adminauth/login").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/role/**").hasAnyRole("ADMIN")
                        .requestMatchers("/category/**").hasAnyRole("ADMIN")
                        .requestMatchers("/product/**").hasAnyRole("ADMIN")
                        .requestMatchers("/author/**").hasAnyRole("ADMIN")
                        .requestMatchers("/orders/**").hasAnyRole("ADMIN")
                        .requestMatchers("/orderdetail/**").hasAnyRole("ADMIN")
                        .requestMatchers("/contact/**").hasAnyRole("ADMIN")
                        .requestMatchers("/forgot/**").hasAnyRole("ADMIN")
                        .requestMatchers("/feedback/**").hasAnyRole("ADMIN")
                        .requestMatchers("/slide/**").hasAnyRole("ADMIN")
                        .requestMatchers("/advertise/**").hasAnyRole("ADMIN")
                        .requestMatchers("/eshopper/**").permitAll()
                        .requestMatchers("/content/**").permitAll()
                        .anyRequest().permitAll())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
