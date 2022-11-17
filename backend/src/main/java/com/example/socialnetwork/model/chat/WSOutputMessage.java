package com.example.socialnetwork.model.chat;

import com.example.socialnetwork.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Embeddable
public class WSOutputMessage {
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, updatable = false)
    private final User fromUser;
    private final String toRoom;
    private final String msg;
    private final LocalDate sentAt;
}
