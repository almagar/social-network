package com.example.socialnetwork.dao;

import com.example.socialnetwork.model.User;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Data access object for the {@link User} entity.
 */
@Repository
public interface UserDAO extends GenericDAO<User, UUID> {
    /**
     * Retrieves a {@link User} by its username.
     * @param username the username.
     * @return return {@link Optional<User>}.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves all the {@link User}s whose usernames matches the given substring.
     * @param username username to be searched.
     * @return a {@link Streamable} of the found {@link User}s.
     */
    Streamable<User> findByUsernameContaining(String username);
}
