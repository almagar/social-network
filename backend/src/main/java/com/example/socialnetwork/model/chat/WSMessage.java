package com.example.socialnetwork.model.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WSMessage {
    private final String from;
    private final String text;
}
