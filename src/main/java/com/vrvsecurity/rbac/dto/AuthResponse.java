package com.vrvsecurity.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) for sending the authentication response.
 * This class represents the structure of the response returned after successful authentication,
 * containing the JWT token and the type of the token (usually "Bearer").
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    /**
     * The JWT token issued after successful authentication.
     * This token is used for subsequent requests to authenticate the user.
     */
    private String token;

    /**
     * The type of the token, typically "Bearer".
     * This is used to indicate the authorization method when passing the token in the request header.
     */
    private String type;
}
