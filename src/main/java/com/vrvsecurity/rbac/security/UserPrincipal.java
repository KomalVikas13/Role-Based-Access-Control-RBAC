package com.vrvsecurity.rbac.security;

import com.vrvsecurity.rbac.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Custom implementation of the {@link UserDetails} interface for Spring Security.
 * This class represents the authenticated user, providing details like the user's roles, password, username, and status.
 * It is used to integrate user data from the application's domain model (i.e., {@link User}) into Spring Security's authentication mechanism.
 */
public class UserPrincipal implements UserDetails {

    private User user;

    /**
     * Constructs a new {@link UserPrincipal} instance using the provided user data.
     *
     * @param user The user object containing user data to be used by Spring Security.
     */
    public UserPrincipal(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities (roles) granted to the user.
     * The roles are extracted from the user's roles and converted to {@link GrantedAuthority}.
     *
     * @return A collection of granted authorities (roles) for the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the password of the user.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username of the user.
     * In this implementation, the username is taken as the user's email.
     *
     * @return The user's email as the username.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Returns the current status of the user.
     * The status could indicate whether the user's account is active, pending, etc.
     *
     * @return The status of the user (e.g., "active", "pending").
     */
    public String getStatus() {
        return user.getStatus();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
