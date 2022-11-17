package com.example.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class WSOutputMessageDTO {
    private UserDTO fromUser;
    private String toRoom;
    private String msg;
    private LocalDate sentAt;
}
