package com.whereitgo.security;

import com.whereitgo.model.User;
import com.whereitgo.model.UserPrincipal;
import com.whereitgo.repository.UserRepo;
import com.whereitgo.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        log.info("Incoming request: {}", request.getRequestURI());

        // 1. No token case
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No JWT token found in request");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. Extract token
            String token = authHeader.substring(7);
            log.info("JWT Token received: {}", token);

            // 3. Extract userId from token
            String userId = jwtService.extractUserId(token);
            log.info("Extracted userId from token: {}", userId);

            if (!jwtService.isTokenValid(token)) {
                log.error("JWT token got expired");
                return;
            }

            // 4. Check security context already set
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                log.info("Loading user details for userId: {}", userId);

                // ⚠️ IMPORTANT: this assumes username == userId in DB layer
                User user = userRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                UserDetails userDetails = new UserPrincipal(user);

                log.info("User loaded: {}", userDetails.getUsername());

                // 5. Create authentication object
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.info("Security context set successfully for userId: {}", userId);
            }

        } catch (Exception e) {
            log.error("JWT processing failed: {}", e.getMessage());
        }

        // 6. Continue request
        filterChain.doFilter(request, response);
    }
}