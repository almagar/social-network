package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Helper class to map between model classes and their related DTO classes.
 */
@Component
public class Mapper {
    /**
     * Converts a {@link User} into a {@link UserDTO}.
     * @param user the {@link User} to be converted.
     * @return the converted {@link UserDTO}.
     */
    public static UserDTO toDTO(User user) {
        String avatarUri = "/user/" + user.getId().toString() + "/avatar";
        return new UserDTO(user.getId().toString(), user.getUsername(), user.getDescription(), avatarUri);
    }

    /**
     * Converts a {@link Post} into a {@link PostDTO}.
     * @param post the {@link Post} to be converted.
     * @return the converted {@link UserDTO}.
     */
    public static PostDTO toDTO(Post post) {
        String imageUri = "/post/" + post.getId().toString() + "/image";
        return new PostDTO(post.getId().toString(), toDTO(post.getUser()), post.getBody(), imageUri,
                post.getCreatedAt(), post.getUpdatedAt());
    }

    /**
     * Converts a {@link UserDTO} into a {@link User}.
     * @param dto the {@link UserDTO} to be converted.
     * @return the converted {@link User}.
     */
    public static User toModel(UserDTO dto) {
        return new User(fromStringToUUID(dto.getId()), dto.getUsername(), dto.getDescription());
    }

    /**
     * Converts a {@link PostDTO} into a {@link Post}.
     * @param dto the {@link PostDTO} to be converted.
     * @return the converted {@link Post}.
     */
    public static Post toModel(PostDTO dto) {
        return new Post(toModel(dto.getCreator()), dto.getBody());
    }

    /**
     * Creates a UUID from the given string if it matches the standard representation.
     * @param id the id to be converted.
     * @return the UUID if succeeded, null otherwise.
     */
    public static UUID fromStringToUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
