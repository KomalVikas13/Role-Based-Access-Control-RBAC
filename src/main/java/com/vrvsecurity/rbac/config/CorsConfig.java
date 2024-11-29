package com.vrvsecurity.rbac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing (CORS) rules.
 * <p>
 * This class configures the allowed origins, methods, headers, and other settings
 * for cross-origin requests, enabling the application to handle requests from
 * different domains securely.
 * </p>
 */

@Configuration // Marks the class as a configuration class to be processed by Spring
public class CorsConfig {

    /**
     * Configures the CORS settings for the application.
     *
     * <p>
     * This method defines the CORS configuration by setting allowed origins, HTTP
     * methods, headers, credentials support, and the exposed headers in the response.
     * It is used to control how cross-origin requests are handled and which origins
     * are allowed to interact with the application.
     * </p>
     *
     * @return a {@link CorsConfigurationSource} containing the CORS settings.
     *         The configuration is applied globally to all URLs (/**).
     */
    @Bean // Marks this method as a Spring Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Create a new CorsConfiguration object
        CorsConfiguration config = new CorsConfiguration();

        // Allow only the specified origin (frontend URL)
        config.addAllowedOrigin("http://localhost:5173"); // Frontend URL (e.g., for React app)

        // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        // Allow all headers (to handle any custom headers)
        config.addAllowedHeader("*");

        // Allow credentials (cookies, HTTP authentication, etc.)
        config.setAllowCredentials(true);

        // Expose the "Authorization" header to the frontend (needed for token-based authentication)
        config.setExposedHeaders(Arrays.asList("Authorization"));

        // Set up the CorsConfigurationSource with the defined configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply configuration to all URLs

        return source; // Return the configured CORS source
    }
}
