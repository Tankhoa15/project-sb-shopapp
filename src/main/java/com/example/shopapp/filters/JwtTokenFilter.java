package com.example.shopapp.filters;

import com.example.shopapp.components.JwtTokenUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        if (isByPassToken(request)) {
            filterChain.doFilter(request, response);
        }

        final String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            final String token = authHeader.substring("Bearer ".length());
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            if(phoneNumber != null &&
                SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails existingUser = userDetailsService.loadUserByUsername(phoneNumber);
            }
        }

    }

    private boolean isByPassToken(@Nonnull HttpServletRequest request){
        final List<Pair<String, String>> byPassToken = Arrays.asList(
            Pair.of(String.format("%s/products",apiPrefix),"GET"),
            Pair.of(String.format("%s/category",apiPrefix),"GET"),
            Pair.of(String.format("%s/users/register",apiPrefix),"POST"),
            Pair.of(String.format("%s/users/login",apiPrefix),"POST")
        );

        for (Pair<String,String> bypassToken : byPassToken) {
            if(request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())){
                return true;
            }
        }
        return false;
    }
}
