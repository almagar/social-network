package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.PostDAO;
import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.model.Post;
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

    public PostDTO create(String body) throws RequiredFieldsException, AuthenticationException {
        if (body == null) {
            throw new RequiredFieldsException();
        }

        Post post = new Post(getLoggedInUser(), body);
        return Mapper.toDTO(postDAO.save(post));
    }

    public PostDTO findById(String id) throws NotFoundException {
        Optional<Post> found = postDAO.findById(Mapper.fromStringToUUID(id));
        if (found.isEmpty()) {
            throw new NotFoundException();
        }

        return Mapper.toDTO(found.get());
    }
}
