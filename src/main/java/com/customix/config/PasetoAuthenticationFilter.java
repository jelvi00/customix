package com.customix.config;

import com.customix.domain.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PasetoAuthenticationFilter extends OncePerRequestFilter {

    private final PasetoManager pasetoManager;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (Objects.isNull(authorization) || !authorization.startsWith("Bearer "))
            filterChain.doFilter(request, response);

        String token = authorization.substring(7);

        String username = pasetoManager.getSubject(token);

        if (Objects.nonNull(username)) {
            var userDetails = userService.loadUserByUsername(username);
            var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else throw new IllegalArgumentException("Invalid token.");

        filterChain.doFilter(request, response);

    }
}
