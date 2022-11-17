package com.example.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatRoomDTO {
    private String id;
    private String name;
    private UserDTO owner;
    private List<UserDTO> users;
}
