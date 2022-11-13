package com.example.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data transfer object for posts.
 */
@Data
@AllArgsConstructor
public class PostDTO {
    private String id;
    private UserDTO creator;
    private String body;
    private String imageUri;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
