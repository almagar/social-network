package com.example.socialnetwork.dao;

import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.chat.ChatRoom;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data access object for the {@link ChatRoom} entity.
 */
public interface ChatRoomDAO extends GenericDAO<ChatRoom, UUID> {
    Optional<ChatRoom> findByName(String name);
    Streamable<ChatRoom> findByOwnerOrderByName(User user);
    Streamable<ChatRoom> findByUsersContainingOrderByName(User user);
}
