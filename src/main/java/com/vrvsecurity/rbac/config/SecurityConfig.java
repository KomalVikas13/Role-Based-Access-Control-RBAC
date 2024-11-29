package com.vrvsecurity.rbac.config;

import com.vrvsecurity.rbac.security.JwtAuthFilter;
import com.vrvsecurity.rbac.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the application.
 * <p>
 * This class configures authentication, authorization, session management, and security filters
 * using Spring Security. It ensures proper security configurations for different roles
 * (e.g., ADMIN, MODERATOR, USER) and JWT-based authentication for stateless communication.
 * </p>
 */
@Configuration
@EnableWebSecurity // Enables Spring Security web configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // Service for loading user details

    @Autowired
    private JwtUtil jwtUtil; // Utility for handling JWT tokens

    @Autowired
    private CorsConfig corsConfig; // CORS configuration for handling cross-origin requests

    /**
     * Configures the security filter chain for the application.
     * <p>
     * This method sets up CORS, disables CSRF, configures access control rules for various endpoints,
     * enables stateless session management, and integrates JWT authentication using the JwtAuthFilter.
     * </p>
     *
     * @param http the {@link HttpSecurity} object for configuring security.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // Sets up CORS configuration
                .csrf(customizer -> customizer.disable()) // Disables CSRF protection as the application uses stateless JWT authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/auth/**").permitAll() // Allows unrestricted access to public and auth-related endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Restricts admin-related endpoints to users with ADMIN role
                        .requestMatchers("/moderator/**").hasRole("MODERATOR") // Restricts moderator-related endpoints to users with MODERATOR role
                        .requestMatchers("/user/**").hasRole("USER") // Restricts user-related endpoints to users with USER role
                        .anyRequest().authenticated()) // Requires authentication for any other request
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Disables session-based authentication
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // Adds the JWT filter before the default authentication filter
                .build();
    }

    /**
     * Provides a JWT authentication filter for verifying JWT tokens in incoming requests.
     * <p>
     * This filter checks the validity of the JWT token in the request and ensures that the
     * authenticated user is authorized to access protected resources.
     * </p>
     *
     * @return a {@link JwtAuthFilter} configured with the JWT utility and user details service.
     */
    @Bean
    public JwtAuthFilter jwtAuthenticationFilter() {
        return new JwtAuthFilter(jwtUtil, userDetailsService); // Returns a JWT filter that uses JWT utility and user details service
    }

    /**
     * Configures the authentication provider for the application.
     * <p>
     * Uses {@link DaoAuthenticationProvider} to authenticate users with the database and
     * validates passwords using a BCrypt encoder.
     * </p>
     *
     * @return an {@link AuthenticationProvider} that handles user authentication.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Sets the user details service for authentication
        provider.setPasswordEncoder(passwordEncoder()); // Sets the password encoder to validate passwords securely

        return provider;
    }

    /**
     * Provides the {@link AuthenticationManager} for handling authentication requests.
     * <p>
     * This bean is used by Spring Security to manage authentication processes.
     * </p>
     *
     * @param configuration the {@link AuthenticationConfiguration} used to retrieve the authentication manager.
     * @return an {@link AuthenticationManager} for managing authentication.
     * @throws Exception if an error occurs during initialization.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // Retrieves the authentication manager from Spring Security's configuration
    }

    /**
     * Configures the password encoder for encoding and validating user passwords.
     * <p>
     * Uses {@link BCryptPasswordEncoder} with a strength of 12 to securely hash passwords.
     * </p>
     *
     * @return a {@link BCryptPasswordEncoder} instance for password encoding.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Returns an encoder with strength 12 for secure password hashing
    }
}
