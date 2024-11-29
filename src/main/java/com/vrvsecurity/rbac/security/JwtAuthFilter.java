package com.vrvsecurity.rbac.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom JWT authentication filter for securing API endpoints.
 * <p>
 * This filter intercepts incoming requests to validate JWT tokens, authenticate users, and populate the security context.
 * It ensures that only requests with valid tokens proceed further in the filter chain.
 * </p>
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for injecting dependencies.
     *
     * @param jwtUtil           Utility class for handling JWT operations like extracting and validating tokens.
     * @param userDetailsService Service to load user details based on username.
     */
    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters each incoming request to validate JWT tokens and authenticate users.
     *
     * @param request     The HTTP request being processed.
     * @param response    The HTTP response being generated.
     * @param filterChain The filter chain to pass the request/response to the next filter.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException      If an error occurs during input/output operations.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Proceed if the Authorization header is missing or does not contain a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username = null;

        try {
            // Extract the username from the JWT
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            handleAuthenticationException(response, e);
            return;
        }

        // Validate the token and set up the security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    // Check if the user's account status is 'pending'
                    if (userDetails instanceof UserPrincipal) {
                        UserPrincipal customUserDetails = (UserPrincipal) userDetails;
                        if ("pending".equalsIgnoreCase(customUserDetails.getStatus())) {
                            // Reject requests from users with 'pending' status
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            Map<String, String> error = new HashMap<>();
                            error.put("error", "Account is pending approval. Please contact admin.");
                            String jsonError = new ObjectMapper().writeValueAsString(error);
                            response.getWriter().write(jsonError);
                            return;
                        }
                    }

                    // Create an authentication token and populate the security context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                handleAuthenticationException(response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Handles authentication exceptions by sending an error response.
     *
     * @param response The HTTP response to send the error details.
     * @param e        The exception that occurred during authentication.
     * @throws IOException If an error occurs during response writing.
     */
    private void handleAuthenticationException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, String> error = new HashMap<>();
        error.put("error", "Authentication failed: " + e.getMessage());
        String jsonError = new ObjectMapper().writeValueAsString(error);
        response.getWriter().write(jsonError);
    }
}
