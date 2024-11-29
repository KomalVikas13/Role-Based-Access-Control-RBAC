package com.vrvsecurity.rbac.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * <p>
 * This class holds the data for the user registration process, including the user's
 * personal information and the roles to be assigned to the user.
 * </p>
 */
@Getter // Lombok annotation to generate getter methods for all fields.
@Setter // Lombok annotation to generate setter methods for all fields.
public class RegistrationRequest {

    /**
     * The full name of the user registering on the platform.
     * This field holds the user's first and last names.
     */
    private String fullName;

    /**
     * The user's cell phone number.
     * This is typically used for contact purposes or user verification.
     */
    private int cellNumber;

    /**
     * The email address provided by the user.
     * This will be used for account identification and authentication.
     */
    private String email;

    /**
     * The password chosen by the user.
     * This will be used for authenticating the user and is securely encoded before storage.
     */
    private String password;

    /**
     * The set of roles to be assigned to the user.
     * These roles determine the user's permissions and access levels within the system.
     */
    private Set<String> roles;
}
