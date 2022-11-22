package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.PostDAO;
import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.IllegalContentTypeException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
     * @return a {@link PostDTO} for the created {@link Post}.
     * @throws RequiredFieldsException if the given body is null.
     * @throws AuthenticationException if an authentication error has occurred.
     * @throws IllegalContentTypeException if the given image has a wrong content-type.
     * @throws IOException if an I/O exception has occurred.
     */
    public PostDTO create(String body, MultipartFile image)
            throws RequiredFieldsException, AuthenticationException, IllegalContentTypeException, IOException {
        if (body == null) {
            throw new RequiredFieldsException();
        }

        Post post;
        if (image == null) {
            post = new Post(getLoggedInUser(), body);
        } else {
            String type = image.getContentType();
            if (type != null && (type.equals("image/png") || type.equals("image/jpeg"))) {
                post = new Post(getLoggedInUser(), body, image.getBytes());
            } else {
                throw new IllegalContentTypeException();
            }
        }

        return Mapper.toDTO(postDAO.save(post));
    }

    /**
     * Stores a new {@link Post} created by the given {@link User}.
     * @param body the body.
     * @param creator the {@link User} creator.
     * @return the created {@link Post} is the creation was successfully.
     * @throws RequiredFieldsException if the given body is null.
     */
    public Post create(String body, User creator) throws RequiredFieldsException {
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
        User loggedInUser = getLoggedInUser();
        Set<User> users = loggedInUser.getFollowing();
        users.add(loggedInUser);
        Page<Post> posts = postDAO.getAllByUsers(users,
                // todo: asc and change in frontend
                PageRequest.of(page, pageSize, Sort.by("created_at").descending()));
        return posts.stream().map(Mapper::toDTO).toList();
    }

    /**
     * Updates the {@link Post} given by its id.
     * @param id the {@link Post} id.
     * @param body the new body.
     * @param image the new image.
     * @return a {@link PostDTO} for the updated {@link Post}.
     * @throws NotFoundException if no {@link Post} was found with the given id.
     * @throws AuthenticationException if the {@link Post} was not created by the authenticated {@link User}.
     * @throws IOException if an I/O exception has occurred.
     */
    public PostDTO update(String id, String body, MultipartFile image)
            throws NotFoundException, AuthenticationException, IOException {
        Post post = postDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        if (!post.getUser().equals(getLoggedInUser())) {
            throw new AuthenticationException();
        }

        boolean updated = false;
        if (body != null) {
            post.setBody(body);
            updated = true;
        }

        if (image != null) {
            String type = image.getContentType();
            if (type != null && (type.equals("image/png") || type.equals("image/jpeg"))) {
                post.setImage(image.getBytes());
                updated = true;
            } else {
                throw new IllegalContentTypeException();
            }
        }

        if (updated) {
            post.setUpdatedAt(LocalDateTime.now());
            postDAO.save(post);
        }

        return Mapper.toDTO(post);
    }

    /**
     * Retrieves the image for a {@link Post} given by its id.
     * @param id the {@link Post}'s id.
     * @return the image represented by a byte array.
     * @throws NotFoundException if no {@link Post} was found with the given id.
     */
    public byte[] getImage(String id) throws NotFoundException {
        Post post = postDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new);
        return post.getImage();
    }
}
