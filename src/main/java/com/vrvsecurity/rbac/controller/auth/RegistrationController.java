package com.vrvsecurity.rbac.controller.auth;

import com.vrvsecurity.rbac.dto.RegistrationRequest;
import com.vrvsecurity.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user registration operations.
 * <p>
 * This class handles the user registration requests. It accepts user details from the
 * client, processes the registration, and returns an appropriate response.
 * </p>
 */
@RestController // Marks this class as a Spring MVC REST controller
@RequestMapping("/auth") // Defines the base URL path for authentication-related endpoints
public class RegistrationController {

    private final UserService userService;

    /**
     * Constructor to initialize the UserService.
     *
     * @param userService the service to handle user registration.
     */
    public RegistrationController(UserService userService){
        this.userService = userService;
    }

    /**
     * Endpoint for user registration.
     * <p>
     * This method receives the user's registration request containing the user details,
     * processes the registration using the UserService, and returns a success or error response.
     * </p>
     *
     * @param request the registration request containing user details (e.g., email, password, etc.).
     * @return a ResponseEntity containing the registration response or an error message.
     */
    @PostMapping("/register") // Maps this method to the POST request at '/auth/register'
    public ResponseEntity<String> registration(@RequestBody RegistrationRequest request){
        try {
            String response = userService.registration(request); // Call the registration service
            return ResponseEntity
                    .status(HttpStatus.CREATED) // Respond with HTTP status 201 Created
                    .body(response); // Return the success message as the response body
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Respond with HTTP status 400 Bad Request in case of error
                    .body(e.getMessage()); // Return the error message in the response body
        }
    }
}
