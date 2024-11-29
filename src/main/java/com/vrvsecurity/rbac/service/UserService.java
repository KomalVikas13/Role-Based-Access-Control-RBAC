package com.vrvsecurity.rbac.service;

import com.vrvsecurity.rbac.Repository.RoleRepository;
import com.vrvsecurity.rbac.Repository.UserRepository;
import com.vrvsecurity.rbac.dto.RegistrationRequest;
import com.vrvsecurity.rbac.dto.UserStatus;
import com.vrvsecurity.rbac.model.Role;
import com.vrvsecurity.rbac.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class responsible for managing user-related operations such as registration, retrieving users by status, and updating user status.
 * <p>
 * This service handles user registration, ensures role-based status management, and provides methods for updating and retrieving user statuses.
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserService with the provided repositories and password encoder.
     *
     * @param userRepository  The repository for managing users in the database.
     * @param roleRepository  The repository for managing roles in the database.
     * @param passwordEncoder The encoder for encrypting user passwords.
     */
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user after validating that the email is not already taken.
     * The user's password is encrypted, roles are assigned based on the request, and a status is set depending on the roles.
     * <p>
     * If the user has an 'admin' or 'moderator' role, their account will have a 'pending' status for admin review.
     * </p>
     *
     * @param request The registration request object containing user details and roles.
     * @return A message indicating whether the registration was successful or if the email is already taken.
     */
    public String registration(RegistrationRequest request) {
        Optional<User> byEmail = userRepository.findByEmail(request.getEmail());
        if(byEmail.isPresent()){
            return "Already had an account with " + request.getEmail() + " email";
        }

        // Initialize the User object with details from the registration request
        User user = new User();
        user.setFullName(request.getFullName());
        user.setCellNumber(request.getCellNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode the password

        // Default status for 'user', updated based on roles
        String status = "active";
        Set<Role> roles = new HashSet<>();

        // Assign roles and set status to 'pending' for admin or moderator roles
        for (String roleName : request.getRoles()) {
            Role role = roleRepository.findByName("ROLE_" + roleName)
                    .orElseThrow(() -> new RuntimeException(roleName + " role not found"));

            roles.add(role); // Add the role to the user's roles

            if ("admin".equalsIgnoreCase(roleName) || "moderator".equalsIgnoreCase(roleName)) {
                status = "pending"; // Set status to 'pending' for admin or moderator
            }
        }

        user.setStatus(status); // Set the user's status
        user.setRoles(roles); // Set the user's roles

        // Save the user to the database
        userRepository.save(user);

        // Return an appropriate message based on the status
        if ("pending".equalsIgnoreCase(status)) {
            return "Registration successful. Your account is pending activation and requires admin review.";
        } else {
            return "Registration successful. You can now log in to your account.";
        }
    }

    /**
     * Retrieves a list of users by their status.
     *
     * @param status The status of users to be retrieved (e.g., 'active', 'pending').
     * @return A list of users with the specified status.
     * @throws RuntimeException If no users are found with the specified status.
     */
    public List<User> getUserByStatus(String status) {
        List<User> users = userRepository.findByStatus(status);
        if(users.isEmpty()){
            throw new RuntimeException("No users found with " + status + " status");
        }
        return users;
    }

    /**
     * Updates the status of a user based on the provided status object.
     * The user is identified by their email, and their status is updated in the database.
     *
     * @param status The UserStatus object containing the email and new status.
     * @return A message indicating the updated status of the user.
     * @throws RuntimeException If the user is not found with the specified email.
     */
    public String updateUserStatus(UserStatus status) {
        Optional<User> byEmail = userRepository.findByEmail(status.getEmail());
        if(byEmail.isEmpty()){
            throw new RuntimeException("User not found");
        }
        User user = byEmail.get();
        user.setStatus(status.getStatus()); // Update the user's status
        userRepository.save(user);

        return "Updated user status to " + status.getStatus();
    }
}
