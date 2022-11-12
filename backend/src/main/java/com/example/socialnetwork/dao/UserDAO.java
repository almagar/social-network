package com.example.socialnetwork.dao;

import com.example.socialnetwork.model.User;
import org.springframework.data.util.Streamable;

import java.util.Optional;
import java.util.UUID;

/**
 * Data access object for the user entity.
 */
public interface UserDAO extends GenericDAO<User, UUID> {
    /**
     * Find a {@link User} by username.
     * @param username the username.
     * @return return {@link Optional<User>}.
     */
    Optional<User> findByUsername(String username);

    /**
     * Search for {@link User}s matched by username.
     * @param username username to search for.
     * @return a {@link Streamable<User>} of the found users.
     */
    Streamable<User> findByUsernameContaining(String username);
}
