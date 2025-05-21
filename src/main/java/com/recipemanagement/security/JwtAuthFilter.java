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

/**
 * JWT authentication filter for Spring Security.
 * Enhanced with better public path handling for cloud deployments.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // List of path prefixes that should skip authentication
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

    // List of exact paths that should skip authentication
    private final List<String> exactPublicPaths = Arrays.asList(
            "/",
            "/favicon.ico",
            "/index.html"
    );

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Determines if a path should skip authentication
     * @param path Request URI path
     * @return true if authentication should be skipped
     */
    private boolean isPublicPath(String path) {
        // Check exact paths first
        if (exactPublicPaths.contains(path)) {
            return true;
        }

        // Check path prefixes
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    /**
     * Main filter method for JWT processing with enhanced path handling
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain Filter chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Log path for debugging (consider removing in production)
        logger.debug("Processing path: " + path);

        // Check for GET /api/recipes requests which should be public
        boolean isPublicApiRecipe = request.getMethod().equals("GET") &&
                path.startsWith("/api/recipes");

        // Skip JWT token validation for public paths
        if (isPublicPath(path) || isPublicApiRecipe) {
            logger.debug("Skipping JWT check for public path: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);

                    // Load user details and create authentication token
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("JWT Filter Error: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed: " + e.getMessage());
        }
    }
}