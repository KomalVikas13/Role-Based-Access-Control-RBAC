package com.vrvsecurity.rbac.Repository;

import com.vrvsecurity.rbac.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * <p>
 * This interface provides methods to perform CRUD operations and custom queries
 * for the Role entity. It extends the {@link JpaRepository} interface, which
 * provides default implementations for common database operations.
 * </p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Custom query method to find a Role by its name.
     * <p>
     * This method returns an {@link Optional} containing the Role entity
     * if a match is found, or an empty {@link Optional} if no match is found.
     * </p>
     *
     * @param role The name of the role to search for.
     * @return An {@link Optional} containing the matching Role entity, if found.
     */
    Optional<Role> findByName(String role);
}
