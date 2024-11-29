package com.vrvsecurity.rbac.controller.admin;

import com.vrvsecurity.rbac.dto.RoleRequest;
import com.vrvsecurity.rbac.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing roles by the admin.
 * <p>
 * This controller handles HTTP requests related to role management, such as adding new roles
 * to the system. It interacts with the {@link RoleService} to perform the actual role-related
 * operations.
 * </p>
 */
@RestController // Marks this class as a Spring MVC REST controller
@RequestMapping("/admin") // Defines the base URL path for admin-related endpoints
public class RoleAdminController {

    private final RoleService roleService; // Service to handle role-related operations

    /**
     * Constructor to initialize RoleService.
     *
     * @param roleService the service handling role-related operations.
     */
    public RoleAdminController(RoleService roleService){
        this.roleService = roleService;
    }

    /**
     * Endpoint to add a new role.
     * <p>
     * This method accepts a {@link RoleRequest} containing the details of the role to be added
     * and delegates the logic to the {@link RoleService}.
     * </p>
     *
     * @param roleRequest the request body containing role details.
     * @return a {@link ResponseEntity} containing the response message.
     */
    @PostMapping("/addRole") // Maps this method to POST requests at '/admin/addRole'
    public ResponseEntity<String> addRole(@RequestBody RoleRequest roleRequest){
        try {
            String response = roleService.addRole(roleRequest); // Delegate the task to RoleService
            return ResponseEntity
                    .status(HttpStatus.CREATED) // Return HTTP status 201 Created
                    .body(response); // Return the response message
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Return HTTP status 400 Bad Request
                    .body(e.getMessage()); // Return the error message
        }
    }

}
