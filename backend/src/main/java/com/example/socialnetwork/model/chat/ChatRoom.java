package com.example.socialnetwork.model.chat;

import com.example.socialnetwork.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@Data
public class ChatRoom {
    @Id
    @Column(unique = true, nullable = false)
    private final UUID id;

    @Column(nullable = false)
    private final String name;

    @JoinColumn(table = "Users", nullable = false, updatable = false, referencedColumnName = "id")
    private final UUID owner;

    @ElementCollection
    @Column(nullable = false)
    private Set<User> users;

    @ElementCollection
    @Column
    private List<WSOutputMessage> messages;
}
