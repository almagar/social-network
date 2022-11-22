package com.example.socialnetwork.model.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WSIncomingMessage {
    private final String fromUser;
    private final String msg;
    private String b64Image;
}
