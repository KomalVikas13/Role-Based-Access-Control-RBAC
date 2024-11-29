package com.vrvsecurity.rbac.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for updating a user's status.
 * <p>
 * This class is used to hold the data necessary to change the status of a user
 * based on their email address and the new status (e.g., "active", "pending").
 * </p>
 */
@Getter // Lombok annotation to generate getter methods for all fields.
@Setter // Lombok annotation to generate setter methods for all fields.
public class UserStatus {

    /**
     * The email address of the user whose status is to be updated.
     * This field is used to identify the user whose status is being changed.
     */
    private String email;

    /**
     * The new status to be assigned to the user.
     * This field can hold values such as "active", "pending", etc., representing
     * the user's current status within the system.
     */
    private String status;
}
