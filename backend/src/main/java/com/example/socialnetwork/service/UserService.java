package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.IllegalContentTypeException;
import com.example.socialnetwork.model.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Service class for the {@link User} entity.
 */
@Service
public class UserService extends AbstractService {
    public UserService(UserDAO userDAO) {
        super(userDAO);
    }

    /**
     * Stores a new {@link User}.
     * @param user the {@link User} to be created.
     */
    public void create(User user) {
        userDAO.save(user);
    }

    /**
     * Updated the authenticated {@link User} profile.
     * @param description the new description.
     * @param avatar the new avatar.
     * @return a {@link UserDTO} of the updated {@link User}.
     * @throws AuthenticationException if an authentication error has occurred.
     * @throws IOException if an I/O exception has occurred.
     */
    public UserDTO update(String description, MultipartFile avatar) throws IOException {
        User loggedInUser = getLoggedInUser();
        boolean updated = false;
        if (description != null) {
            loggedInUser.setDescription(description);
            updated = true;
        }

        if (avatar != null) {
            String type = avatar.getContentType();
            if (type != null && (type.equals("image/png") || type.equals("image/jpeg"))) {
                loggedInUser.setAvatar(avatar.getBytes());
                updated = true;
            } else {
                throw new IllegalContentTypeException();
            }
        }

        if (updated) {
            userDAO.save(loggedInUser);
        }

        return Mapper.toDTO(loggedInUser);
    }

    /**
     * Deletes the given {@link User}.
     * @param user the {@link User}.
     */
    public void delete(User user) {
        userDAO.delete(user);
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

    /**
     * Retrieves the authenticated {@link User}.
     * @return a {@link UserDTO} for the authenticated {@link User}.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    public UserDTO getProfile() throws AuthenticationException {
        return Mapper.toDTO(getLoggedInUser());
    }

    /**
     * Retrieves the avatar for the authenticated {@link User}.
     * @return the avatar represented by a byte array.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    public byte[] getAvatar() throws AuthenticationException {
        return getLoggedInUser().getAvatar();
    }

    /**
     * Retrieves the avatar for the {@link User} given by its id.
     * @param id the {@link User} id.
     * @return the avatar represented by a byte array.
     * @throws NotFoundException if no {@link User} vas found by the given username.
     */
    public byte[] getAvatar(String id) throws NotFoundException {
        User user = userDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        return user.getAvatar();
    }

    /**
     * Retrieves all the {@link User}s whose usernames matches the given substring.
     * @param username the username.
     * @return a {@link List} of the found {@link User}s.
     */
    public List<UserDTO> searchByUsername(String username) {
        return userDAO.findByUsernameContaining(username).stream().map(Mapper::toDTO).toList();
    }

    /**
     * Adds the {@link User} with the given id to the authenticated {@link User}'s following list.
     * @param id the {@link User}'s id.
     * @throws NotFoundException if no {@link User} was found by the given id.
     * @throws IllegalStateException if the authenticated {@link User} already follows the given {@link User}.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    @Transactional
    public void followUser(String id) throws NotFoundException, IllegalStateException, AuthenticationException {
        User user = userDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        User loggedInUser = getLoggedInUser();
        if (loggedInUser.getFollowing().contains(user)) {
            throw new IllegalStateException();
        }

        loggedInUser.follow(user);
        userDAO.save(loggedInUser);
    }

    /**
     * Removes the {@link User} with the given id to from the authenticated {@link User}'s following list.
     * @param id the {@link User}'s id.
     * @throws NotFoundException if no {@link User} was found by the given id.
     * @throws IllegalStateException if the authenticated {@link User} don't follow this {@link User}.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    @Transactional
    public void unFollowUser(String id) throws NotFoundException, IllegalStateException, AuthenticationException {
        User user = userDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        User loggedInUser = getLoggedInUser();
        if (!loggedInUser.getFollowing().contains(user)) {
            throw new IllegalStateException();
        }

        loggedInUser.unfollow(user);
        userDAO.save(loggedInUser);
    }

    /**
     * Retrieves the authenticated {@link User}'s followings.
     * @return a {@link Collection} of {@link UserDTO}s.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    public Collection<UserDTO> getFollowing() throws AuthenticationException {
        User loggedInUser = getLoggedInUser();
        return loggedInUser.getFollowing().stream().map(Mapper::toDTO).toList();
    }

    /**
     * Retrieves the followings of the {@link User} given by its id.
     * @param id the {@link User} id.
     * @return a {@link Collection} of {@link UserDTO}s.
     * @throws NotFoundException if no {@link User} was found with the given id.
     */
    public Collection<UserDTO> getFollowing(String id) throws NotFoundException {
        User user = userDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        return user.getFollowing().stream().map(Mapper::toDTO).toList();
    }

    /**
     * Retrieves the authenticated {@link User}'s followers.
     * @return a {@link Collection} of {@link UserDTO}s.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    public Collection<UserDTO> getFollowers() throws AuthenticationException {
        User loggedInUser = getLoggedInUser();
        return loggedInUser.getFollowers().stream().map(Mapper::toDTO).toList();
    }

    /**
     * Retrieves the followers of the {@link User} given by its id.
     * @param id the {@link User} id.
     * @return a {@link Collection} of {@link UserDTO}s.
     * @throws NotFoundException if no {@link User} was found with the given id.
     */
    public Collection<UserDTO> getFollowers(String id) throws NotFoundException {
        User user = userDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        return user.getFollowers().stream().map(Mapper::toDTO).toList();
    }

    /**
     * Validates whether the authenticated {@link User} is following the {@link User} given by its id.
     * @param id the {@link User} id.
     * @return true if following, null if the given id belong to the authenticated {@link User}.
     * @throws NotFoundException if no {@link User} was found with the given id.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    public Boolean isFollowing(String id) throws NotFoundException, AuthenticationException {
        User user = userDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        return user.equals(getLoggedInUser()) ? null : getLoggedInUser().getFollowing().contains(user);
    }
}
