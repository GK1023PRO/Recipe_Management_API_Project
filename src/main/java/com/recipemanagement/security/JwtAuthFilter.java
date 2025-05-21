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

/**
 * JWT authentication filter for Spring Security.
 * Processes JWT tokens in incoming requests and sets up security context.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Main filter method for JWT processing
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain Filter chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Define public paths that don't need authentication
        String path = request.getRequestURI();
        logger.debug("Processing path: " + path);

        // List of paths that don't require authentication - EXPANDED LIST
        if (path.equals("/") ||
                path.equals("/index.html") ||
                path.startsWith("/OOPDocumentationJavaDoc/") ||
                path.startsWith("/static/") ||
                path.startsWith("/api/auth") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/docs") ||
                path.startsWith("/javadoc") ||
                path.startsWith("/actuator") ||
                path.startsWith("/error")) {
            logger.debug("Skipping JWT check for: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);

                    // Load user details and create authentication token with proper authorities
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Continue the filter chain regardless of token presence
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("JWT Filter Error: ", e);
            // For static resources, continue anyway
            if (path.startsWith("/OOPDocumentationJavaDoc/") || path.startsWith("/static/")) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authentication failed: " + e.getMessage());
            }
        }
    }
}