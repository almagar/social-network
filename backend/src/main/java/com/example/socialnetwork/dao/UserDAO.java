package com.example.socialnetwork.dao;

import com.example.socialnetwork.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDAO extends GenericDAO<User, UUID> {
    boolean existsById(UUID uuid);

    Optional<User> findByUsername(String username);
}
