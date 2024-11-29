package com.vrvsecurity.rbac.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for adding role requests.
 * <p>
 * This class is used to encapsulate the data for requests that involve roles,
 * such as creating a role.
 * </p>
 */
@Getter // Lombok annotation to generate getter methods for all fields.
@Setter // Lombok annotation to generate setter methods for all fields.
public class RoleRequest {

    /**
     * The name of the role to be added.
     * This field contains the role name (e.g., "admin", "user")
     * that will be assigned to a user or created in the system.
     */
    private String role;
}
