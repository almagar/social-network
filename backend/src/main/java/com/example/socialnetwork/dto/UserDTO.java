package com.example.socialnetwork.dto;

import lombok.Data;

/**
 * Data transfer object for users.
 */
@Data
public class UserDTO {
    private String id;
    private String username;
    private String description;

    public UserDTO(String id, String username, String description) {
        this.id = id;
        this.username = username;
        this.description = description;
    }
}
