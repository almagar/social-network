package com.example.socialnetwork.model.chat;

import com.example.socialnetwork.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class ChatRoom {
    public ChatRoom(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private final String name;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, updatable = false)
    private final User owner;

    @ElementCollection
    @Column(nullable = false)
    private Set<User> users = new HashSet<>();

    @ElementCollection
    @Column
    private List<WSOutputMessage> messages = new ArrayList<>();

    public void addUserToRoom(User user) {
        this.users.add(user);
    }
}
