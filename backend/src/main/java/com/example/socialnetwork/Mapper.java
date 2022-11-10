package com.example.socialnetwork;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Mapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId().toString(), user.getUsername(), user.getDescription());
    }

    public static User toUser(UserDTO dto) {
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
