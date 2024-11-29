package com.vrvsecurity.rbac.controller.auth;

import com.vrvsecurity.rbac.dto.AuthResponse;
import com.vrvsecurity.rbac.dto.LoginRequest;
import com.vrvsecurity.rbac.security.JwtUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Controller for authentication operations related to login and token generation.
 * <p>
 * This class handles the login request from users, authenticates them using their email
 * and password, and generates a JWT token if the authentication is successful.
 * </p>
 */
@RestController // Marks this class as a Spring MVC REST controller
@RequestMapping("/auth") // Defines the base URL path for authentication-related endpoints
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager; // Manages authentication processes

    @Autowired
    private JwtUtil jwtUtil; // Utility class to generate JWT tokens

    @Autowired
    private UserDetailsService userDetailsService; // Loads user details by username


    /**
     * Endpoint for user login.
     * <p>
     * This method receives the user's login request (email and password),
     * authenticates the user, and generates a JWT token if authentication is successful.
     * If authentication fails, an error message is returned.
     * </p>
     *
     * @param request the login request containing user email and password.
     * @return a ResponseEntity containing the JWT token if login is successful, or an error message.
     */
    @PostMapping("/login") // Maps this method to the POST request at '/auth/login'
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticating the user using email and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Load user details after successful authentication
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            // Generate JWT token for the authenticated user
            String jwt = jwtUtil.generateToken(userDetails);

            // Return the JWT token wrapped in a response
            return ResponseEntity.ok(new AuthResponse(jwt, "Bearer"));

        } catch (AuthenticationException e) {
            // If authentication fails, return a 401 Unauthorized response with an error message
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
}
