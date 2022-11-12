package com.example.socialnetwork.dao;

import com.example.socialnetwork.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDAO extends GenericDAO<User, UUID> {
    Optional<User> findByUsername(String username);
}
