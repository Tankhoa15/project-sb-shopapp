package com.example.shopapp.configuration;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.POST;

import com.example.shopapp.entity.Role;
import com.example.shopapp.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http
             .csrf(AbstractHttpConfigurer::disable)
             .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
             .authorizeHttpRequests(requests -> {
                requests
                    .requestMatchers(
                         String.format("%s/users/register", apiPrefix),
                         String.format("%s/users/login", apiPrefix),
                         String.format("%s/category/**",apiPrefix),
                         String.format("%s/products/**",apiPrefix)
                    )
                    .permitAll()

                    .requestMatchers(POST,
                            String.format("%s/category/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                    .requestMatchers(PUT,
                            String.format("%s/category/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                    .requestMatchers(DELETE,
                            String.format("%s/category/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                    .requestMatchers(POST,
                            String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                    .requestMatchers(PUT,
                            String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                    .requestMatchers(DELETE,
                            String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                    .requestMatchers(POST,
                         String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                    .requestMatchers(GET,
                         String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
                    .requestMatchers(PUT,
                         String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                    .requestMatchers(DELETE,
                         String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                    .requestMatchers(POST,
                            String.format("%s/order-details/**", apiPrefix)).hasRole(Role.ADMIN)
                    .requestMatchers(GET,
                            String.format("%s/order-details/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
                    .requestMatchers(PUT,
                            String.format("%s/order-details/**", apiPrefix)).hasRole(Role.ADMIN)
                    .requestMatchers(DELETE,
                            String.format("%s/order-details/**", apiPrefix)).hasRole(Role.ADMIN)
                    .anyRequest().authenticated();
             });
         return http.build();
    }
}
