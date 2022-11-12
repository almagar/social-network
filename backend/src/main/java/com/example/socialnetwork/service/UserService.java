package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The service class for the {@link User} entity.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    /**
     * Checks if a user exists.
     * @param id the id of the user to check for.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExists(UUID id) {
        return userDAO.existsById(id);
    }

    /**
     * Get all users.
     * @return all users.
     */
    public List<UserDTO> getAll() {
        return userDAO.findAll().stream().map(Mapper::toDTO).toList();
    }

    /**
     * Get a user by username.
     * @param username the username for the user to get.
     * @return a {@link UserDTO} for the found user.
     * @throws NotFoundException if no user by the username was found.
     */
    public UserDTO getByUsername(String username) throws NotFoundException {
        User user = userDAO.findByUsername(username).orElseThrow(NotFoundException::new);
        return Mapper.toDTO(user);
    }

    /**
     * Search for usernames containing username.
     * @param username usernames to search for.
     * @return a list of found users.
     */
    public List<UserDTO> searchByUsername(String username) {
        return userDAO.findByUsernameContaining(username).stream().map(Mapper::toDTO).toList();
    }

    /**
     * Create a new user.
     * @param user info for the user to create.
     */
    public void createUser(User user) {
        userDAO.save(user);
    }
}
