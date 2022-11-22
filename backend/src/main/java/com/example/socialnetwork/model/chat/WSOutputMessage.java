package com.example.socialnetwork.model.chat;

import com.example.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Embeddable
public class WSOutputMessage {
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, updatable = false)
    private User fromUser;
    private String toRoom;
    private String msg;
    private LocalDate sentAt = LocalDate.now();
    private UUID id;
    private String b64Image;

    public WSOutputMessage(User fromUser, String toRoom, String msg, LocalDate sentAt, String b64Image) {
        this.fromUser = fromUser;
        this.toRoom = toRoom;
        this.msg = msg;
        this.sentAt = sentAt;
        this.b64Image = b64Image;
        this.id = UUID.randomUUID();
    }

    public WSOutputMessage(User fromUser, String toRoom, String msg, LocalDate sentAt) {
        this.fromUser = fromUser;
        this.toRoom = toRoom;
        this.msg = msg;
        this.sentAt = sentAt;
        this.b64Image = null;
        this.id = UUID.randomUUID();
    }
}
