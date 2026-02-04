package com.todoapp.config;

import com.todoapp.domain.entity.User;
import com.todoapp.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Skip authentication for OPTIONS preflight requests (CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.debug("Skipping authentication for OPTIONS preflight request");
            filterChain.doFilter(request, response);
            return;
        }
        
        // Skip authentication for public endpoints
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        // Normalize path (remove context path if present)
        String normalizedPath = path.startsWith(contextPath) ? path.substring(contextPath.length()) : path;
        
        if (normalizedPath.startsWith("/auth/") || 
            normalizedPath.startsWith("/health/") || 
            path.startsWith("/h2-console") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/api-docs") ||
            path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Always log for troubleshooting (not just debug)
        log.info("JwtAuthenticationFilter processing request - Path: {}, Normalized: {}, Method: {}", path, normalizedPath, request.getMethod());
        
        try {
            // Get user ID from X-User-Id header (sent by frontend)
            String userId = request.getHeader("X-User-Id");
            String token = getTokenFromRequest(request);
            
            // Log all headers for troubleshooting
            log.info("Request headers - X-User-Id: {}, Authorization: {}, All headers: {}", 
                userId != null ? userId : "MISSING", 
                token != null ? "present" : "absent",
                java.util.Collections.list(request.getHeaderNames()));
            
            // If we have a user ID header, use it to authenticate
            if (StringUtils.hasText(userId)) {
                log.info("Attempting to authenticate user with ID: {}", userId);
                User user = userRepository.findById(userId).orElse(null);
                if (user != null && user.isActive()) {
                    setAuthentication(user, request);
                    log.info("✓ Successfully authenticated user: {} (ID: {}) for path: {}", user.getUsername(), userId, normalizedPath);
                } else {
                    log.error("✗ User not found or inactive for ID: {}. Path: {}", userId, normalizedPath);
                    // Clear any existing authentication
                    SecurityContextHolder.clearContext();
                }
            } else {
                // Try to extract user from token if it contains user info
                // For now, if no user ID header, log and continue (will fail auth check)
                log.warn("✗ No X-User-Id header found in request to: {}. Method: {}", normalizedPath, request.getMethod());
                if (token != null) {
                    log.warn("Token present but no user ID header - cannot authenticate");
                } else {
                    log.warn("No token and no user ID header - request will be rejected");
                }
                // Clear any existing authentication
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            log.error("✗ Error setting authentication for path: {}", normalizedPath, e);
            // Clear security context on error
            SecurityContextHolder.clearContext();
        }
        
        // Log security context state after processing
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("✓ Security context has authentication: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            log.warn("✗ Security context has NO authentication - request will be rejected");
        }
        
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void setAuthentication(User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(
                user, 
                null, 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Security context set for user: {} with role: {}", user.getUsername(), user.getRole());
        
        // Verify it was set
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("FAILED to set authentication in security context!");
        }
    }
}
