package com.example.socialnetwork.model.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Embeddable
public class WSOutputMessage {
    private final String from;
    private final String to;
    private final String text;
    private final LocalDate createdAt;
}
