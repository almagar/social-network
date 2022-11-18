package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.PostDAO;
import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

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

    public Post create(String body, User creator) {
        if (body == null) {
            throw new RequiredFieldsException();
        }

        Post post = new Post(creator, body);
        return postDAO.save(post);
    }

    /**
     * Retrieves a {@link Post} by its id.
     * @param id the id.
     * @return a {@link PostDTO} for the found {@link Post}.
     * @throws NotFoundException if no {@link Post} was found.
     */
    public PostDTO findById(String id) throws NotFoundException {
        Post post = postDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);

        return Mapper.toDTO(post);
    }

    /**
     * Retrieves a list of {@link PostDTO}s by the given username.
     * @param username the username.
     * @param page the page index.
     * @param pageSize the page size.
     * @return a {@link Collection} of {@link PostDTO}s.
     * @throws NotFoundException if no {@link User} was found with the given username.
     */
    @Transactional
    public Collection<PostDTO> getPostsByUsername(String username, int page, int pageSize) throws NotFoundException {
        User user = userDAO.findByUsername(username).orElseThrow(NotFoundException::new);
        Page<Post> posts = postDAO.getAllByUserId(
                user.getId(), PageRequest.of(page, pageSize, Sort.by("createdAt")));
        return posts.stream().map(Mapper::toDTO).toList();
    }

    /**
     * Retrieves a list of the most recent posts, posted by the {@link User}s followed by the authenticated
     * {@link User}.
     * @param page the page index.
     * @param pageSize the page size.
     * @return a {@link Collection} of {@link PostDTO}s.
     * @throws AuthenticationException if an authentication error has occurred.
     */
    public Collection<PostDTO> getPostsByFollowing(int page, int pageSize) throws AuthenticationException {
        Page<Post> posts = postDAO.getAllByUsers(getLoggedInUser().getFollowing(),
                PageRequest.of(page, pageSize, Sort.by("created_at")));
        return posts.stream().map(Mapper::toDTO).toList();
    }
}
