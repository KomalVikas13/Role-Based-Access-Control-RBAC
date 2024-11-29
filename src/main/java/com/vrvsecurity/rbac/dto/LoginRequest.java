package com.vrvsecurity.rbac.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) for handling login requests.
 * This class represents the structure of the data received from the client when attempting to log in.
 * It contains the email and password provided by the user.
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * The email address of the user attempting to log in.
     * This is used to identify the user in the authentication process.
     */
    private String email;

    /**
     * The password provided by the user for authentication.
     * It is compared against the stored password to verify the user's identity.
     */
    private String password;
}
