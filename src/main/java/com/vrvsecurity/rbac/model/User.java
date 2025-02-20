package com.vrvsecurity.rbac.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Entity class representing a user in the system.
 * <p>
 * This class maps to a table in the database that holds information about users.
 * Each user has unique attributes like email and a password, as well as associated roles for RBAC.
 * </p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    /**
     * Unique identifier for the user.
     * Auto-generated by the database using an identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    /**
     * Full name of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * Contact number of the user.
     * This field is optional and not enforced by constraints.
     */
    private int cellNumber;

    /**
     * Email address of the user.
     * Must be unique and cannot be null.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Encrypted password of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Status of the user (e.g., active, inactive).
     * This field is optional and has no constraints.
     */
    private String status;

    /**
     * Roles assigned to the user.
     * <p>
     * Defined as a many-to-many relationship with the {@link Role} entity.
     * Roles are eagerly fetched whenever the user is loaded.
     * </p>
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key referencing user_id in this entity
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key referencing role_id in the Role entity
    )
    private Set<Role> roles;
}
