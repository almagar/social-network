package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Helper class to map between model classes and their related DTO classes.
 */
@Component
class Mapper {
    /**
     * Convert a {@link User} into a {@link UserDTO}.
     * @param user the user to be converted.
     * @return the converted user.
     */
    public static UserDTO toDTO(User user) {
        String avatarUri = "/user" + user.getId().toString() + "/avatar";
        return new UserDTO(user.getId().toString(), user.getUsername(), user.getDescription(), avatarUri);
    }

    /**
     * Convert a {@link UserDTO} into a {@link User}.
     * @param dto the dto to be converted.
     * @return the converted dto.
     */
    public static User toModel(UserDTO dto) {
        return new User(fromStringToUUID(dto.getId()), dto.getUsername(), dto.getDescription());
    }

    private static UUID fromStringToUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
