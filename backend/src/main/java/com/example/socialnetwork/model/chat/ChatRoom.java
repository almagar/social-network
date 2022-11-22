package com.example.socialnetwork.model.chat;

import com.example.socialnetwork.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, updatable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rooms_users", joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    private List<WSOutputMessage> messages = new ArrayList<>();

    public ChatRoom(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public void addUserToRoom(User user) {
        this.users.add(user);
    }

    public void addMessage(WSOutputMessage message) {
        this.messages.add(message);
    }
}
