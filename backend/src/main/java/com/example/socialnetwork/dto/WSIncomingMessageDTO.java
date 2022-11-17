package com.example.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WSIncomingMessageDTO {
    private String fromUser;
    private String msg;
}
