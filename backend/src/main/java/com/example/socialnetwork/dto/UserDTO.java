package com.example.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for users.
 */
@Data
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String description;
    private String avatarUri;
}
