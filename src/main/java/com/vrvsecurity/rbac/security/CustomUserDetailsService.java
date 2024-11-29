package com.vrvsecurity.rbac.security;

import com.vrvsecurity.rbac.Repository.UserRepository;
import com.vrvsecurity.rbac.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of the {@link UserDetailsService} interface for Spring Security.
 * <p>
 * This service is used by Spring Security to load user-specific data during authentication.
 * It fetches user details from the database using the {@link UserRepository}.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor to inject the {@link UserRepository}.
     *
     * @param userRepository The repository to access user data.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user's details by their email address.
     * <p>
     * This method is called during the authentication process to retrieve user data
     * and convert it into a {@link UserDetails} object for Spring Security.
     * </p>
     *
     * @param email The email address of the user to be loaded.
     * @return A {@link UserDetails} object containing user information.
     * @throws UsernameNotFoundException If no user is found with the given email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with '" + email + "' email");
        }
        return new UserPrincipal(user.get());
    }
}
