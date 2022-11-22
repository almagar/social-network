package com.example.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WSIncomingMessageDTO {
    private String fromUser;
    private String msg;
    private String b64Image;
}
