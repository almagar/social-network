package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for the {@link User} entity.
 */
@Service
public class UserService extends AbstractService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        super(userDAO);
        this.userDAO = userDAO;
    }

    /**
     * Stores a new {@link User}.
     * @param user the {@link User} to be created.
     */
    public void create(User user) {
        userDAO.save(user);
    }

    /**
     * Determines whether a {@link User} exists by an id.
     * @param id the id.
     * @return true if the {@link User} exists, false otherwise.
     */
    public boolean exists(UUID id) {
        return userDAO.existsById(id);
    }

    /**
     * Retrieves all {@link User}s.
     * @return a list of {@link User}s.
     */
    public List<UserDTO> getAll() {
        return userDAO.findAll().stream().map(Mapper::toDTO).toList();
    }

    /**
     * Retrieves a {@link User} by its username.
     * @param username the username.
     * @return a {@link UserDTO} for the found {@link User}.
     * @throws NotFoundException if no {@link User} vas found by the given username.
     */
    public UserDTO getByUsername(String username) throws NotFoundException {
        User user = userDAO.findByUsername(username).orElseThrow(NotFoundException::new);
        return Mapper.toDTO(user);
    }

    public UserDTO getProfile() throws AuthenticationException {
        return Mapper.toDTO(getLoggedInUser());
    }

    /**
     * Retrieves all the {@link User}s whose usernames matches the given substring.
     * @param username the username.
     * @return a {@link List} of the found {@link User}s.
     */
    public List<UserDTO> searchByUsername(String username) {
        return userDAO.findByUsernameContaining(username).stream().map(Mapper::toDTO).toList();
    }
}
