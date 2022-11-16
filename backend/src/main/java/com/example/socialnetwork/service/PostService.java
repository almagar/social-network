package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.PostDAO;
import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for the {@link Post} entity.
 */
@Service
public class PostService extends AbstractService {
    private final PostDAO postDAO;

    public PostService(UserDAO userDAO, PostDAO postDAO) {
        super(userDAO);
        this.postDAO = postDAO;
    }

    /**
     * Stores a new {@link Post} created by the authenticated {@link User}.
     * @param body the body.
     * @return the created {@link PostDTO}.
     * @throws RequiredFieldsException if the given body is null.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    public PostDTO create(String body) throws RequiredFieldsException, AuthenticationException {
        if (body == null) {
            throw new RequiredFieldsException();
        }

        Post post = new Post(getLoggedInUser(), body);
        return Mapper.toDTO(postDAO.save(post));
    }

    /**
     * Retrieves a {@link Post} by its id.
     * @param id the id.
     * @return a {@link PostDTO} for the found {@link Post}.
     * @throws NotFoundException
     */
    public PostDTO findById(String id) throws NotFoundException {
        Optional<Post> found = postDAO.findById(Mapper.fromStringToUUID(id));
        if (found.isEmpty()) {
            throw new NotFoundException();
        }

        return Mapper.toDTO(found.get());
    }
}
