package com.recipemanagement.security;

import com.recipemanagement.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Simplified public paths list for more reliable matching
    private final List<String> publicPaths = Arrays.asList(
            "/api/auth",
            "/v3/api-docs",
            "/swagger-ui",
            "/OOPDocumentationJavaDoc",
            "/api/health",
            "/error",
            "/docs",
            "/javadoc",
            "/api/users",
            "/actuator"
    );

    private final List<String> exactPublicPaths = Arrays.asList(
            "/",
            "/favicon.ico",
            "/index.html"
    );

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    private boolean isPublicPath(String path) {
        // Check exact paths
        if (exactPublicPaths.contains(path)) {
            return true;
        }

        // Check prefixes
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Handle recipe GET requests separately to ensure they're public
        boolean isPublicApiRecipe = request.getMethod().equals("GET") &&
                (path.equals("/api/recipes") || path.startsWith("/api/recipes/"));

        // Skip JWT validation for public paths
        if (isPublicPath(path) || isPublicApiRecipe) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtUtil.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("JWT Authentication Error: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed");
        }
    }
}