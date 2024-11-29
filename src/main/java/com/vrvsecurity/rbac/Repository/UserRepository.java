package com.vrvsecurity.rbac.Repository;

import com.vrvsecurity.rbac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * <p>
 * This interface provides methods to perform CRUD operations and custom queries
 * for the User entity. It extends {@link JpaRepository}, which includes
 * common database operation implementations.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * <p>
     * Email is a unique identifier for users, so this query is expected
     * to return at most one result wrapped in an {@link Optional}.
     * </p>
     *
     * @param email The email address of the user.
     * @return An {@link Optional} containing the matching User, if found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves all users with a specific status.
     * <p>
     * This method is useful for filtering users based on their status
     * (e.g., "active", "inactive").
     * </p>
     *
     * @param status The status value to filter users by.
     * @return A {@link List} of users matching the specified status.
     */
    List<User> findByStatus(String status);
}
