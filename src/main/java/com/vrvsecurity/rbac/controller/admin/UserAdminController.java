package com.vrvsecurity.rbac.controller.admin;

import com.vrvsecurity.rbac.dto.UserStatus;
import com.vrvsecurity.rbac.model.User;
import com.vrvsecurity.rbac.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling administrative operations related to user management.
 * <p>
 * This class provides endpoints for updating user statuses and retrieving users based on their status.
 * It is designed for administrative operations where only admins can update user statuses or view users
 * by their status.
 * </p>
 */
@RestController // Marks this class as a Spring MVC REST controller
@RequestMapping("/admin") // Defines the base URL path for admin-related endpoints
public class UserAdminController {

    private final UserService userService; // Service to handle user-related operations

    /**
     * Constructor to initialize the UserService.
     *
     * @param userService the service to handle user operations.
     */
    public UserAdminController(UserService userService){
        this.userService = userService;
    }

    /**
     * Endpoint to update a user's status.
     * <p>
     * This method allows an admin to update the status of a user, such as activating or deactivating the account.
     * </p>
     *
     * @param status the UserStatus object containing the email and the new status.
     * @return a ResponseEntity containing a success or error message.
     */
    @PostMapping("/updateUserStatus") // Maps this method to POST requests at '/admin/updateUserStatus'
    public ResponseEntity<String> updateUserStatus(@RequestBody UserStatus status){
        try {
            String response = userService.updateUserStatus(status);
            return ResponseEntity
                    .status(HttpStatus.OK) // Return HTTP status 200 OK
                    .body(response); // Return the response message
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Return HTTP status 400 Bad Request
                    .body(e.getMessage()); // Return the error message
        }
    }

    /**
     * Endpoint to retrieve users based on their status.
     * <p>
     * This method allows an admin to view a list of users with a specific status, such as 'active', 'pending', or 'inactive'.
     * </p>
     *
     * @param status the status to filter users by.
     * @return a ResponseEntity containing a list of users with the given status or an error message.
     */
    @GetMapping("/users/{status}") // Maps this method to GET requests at '/admin/users/{status}'
    public ResponseEntity<?> getUserByStatus(@PathVariable String status){
        try {
            List<User> response = userService.getUserByStatus(status); // Fetch users by status
            return ResponseEntity
                    .status(HttpStatus.OK) // Return HTTP status 200 OK
                    .body(response); // Return the list of users
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Return HTTP status 400 Bad Request
                    .body(e.getMessage()); // Return the error message
        }
    }
}
