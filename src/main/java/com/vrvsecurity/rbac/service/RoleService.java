package com.vrvsecurity.rbac.service;

import com.vrvsecurity.rbac.Repository.RoleRepository;
import com.vrvsecurity.rbac.dto.RoleRequest;
import com.vrvsecurity.rbac.model.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class that manages role-related operations such as adding new roles.
 * <p>
 * This service handles the logic of adding new roles to the system. It ensures roles are unique by checking the database
 * for existing roles before creating a new one. Roles are stored in uppercase for consistency.
 * </p>
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Constructs a new RoleService with the provided RoleRepository.
     *
     * @param roleRepository The repository for managing roles in the database.
     */
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    /**
     * Adds a new role to the system after validating that it does not already exist.
     * The role name is converted to uppercase before being stored in the database.
     *
     * @param roleRequest The role request object containing the role name to be added.
     * @return A message indicating whether the role was added successfully or if it already exists.
     * @throws RuntimeException If the role already exists in the database.
     */
    public String addRole(RoleRequest roleRequest) {
        // Convert the role name to uppercase for case-insensitive comparison
        String roleName = "ROLE_" + roleRequest.getRole().toUpperCase();

        // Check if the role already exists in the database (case-insensitive)
        Optional<Role> byName = roleRepository.findByName(roleName);
        if (byName.isPresent()) {
            throw new RuntimeException(roleRequest.getRole().toUpperCase() + " already exists");
        }

        // If the role does not exist, create and save it in uppercase
        Role role = new Role();
        role.setName(roleName);  // Store the role in uppercase
        roleRepository.save(role);

        return roleRequest.getRole().toUpperCase() + " role added";  // Return a message indicating the role was added
    }
}
