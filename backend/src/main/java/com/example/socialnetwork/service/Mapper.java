package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.*;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.chat.ChatRoom;
import com.example.socialnetwork.model.chat.WSIncomingMessage;
import com.example.socialnetwork.model.chat.WSOutputMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Helper class to map between model classes and their related DTO classes.
 */
@Component
public class Mapper {
    /**
     * Converts a {@link User} into a {@link UserDTO}.
     * @param user the {@link User} to be converted.
     * @return the converted {@link UserDTO}.
     */
    public static UserDTO toDTO(User user) {
        String avatarUri = user.getAvatar() != null && user.getAvatar().length > 0 ? "/user/" + user.getId().toString() + "/avatar" : null;
        return new UserDTO(user.getId().toString(), user.getUsername(), user.getFirstname(), user.getLastname(),
                user.getDescription(), avatarUri, user.getFollowing().size(), user.getFollowers().size());
    }

    /**
     * Converts a {@link Post} into a {@link PostDTO}.
     * @param post the {@link Post} to be converted.
     * @return the converted {@link UserDTO}.
     */
    public static PostDTO toDTO(Post post) {
        String imageUri = post.getImage() != null && post.getImage().length > 0 ? "/post/" + post.getId().toString() + "/image" : null;
        return new PostDTO(post.getId().toString(), toDTO(post.getUser()), post.getBody(), imageUri,
                post.getCreatedAt(), post.getUpdatedAt());
    }

    /**
     * Converts a {@link ChatRoom} into a {@link ChatRoomDTO}.
     * @param chatRoom the {@link ChatRoom} to be converted.
     * @return the converted {@link ChatRoomDTO}.
     */
    public static ChatRoomDTO toDTO(ChatRoom chatRoom) {
        return new ChatRoomDTO(
                chatRoom.getId().toString(),
                chatRoom.getName(),
                toDTO(chatRoom.getOwner()),
                chatRoom.getUsers().stream().map(Mapper::toDTO).toList());
    }

    /**
     * Converts a {@link WSIncomingMessage} into a {@link WSIncomingMessageDTO}.
     * @param wsIncomingMessage the {@link WSIncomingMessage} to be converted.
     * @return the converted {@link WSOutputMessageDTO}.
     */
    public static WSIncomingMessageDTO toDTO(WSIncomingMessage wsIncomingMessage) {
        return new WSIncomingMessageDTO(wsIncomingMessage.getFromUser(), wsIncomingMessage.getMsg(), wsIncomingMessage.getB64Image());
    }

    /**
     * Converts a {@link WSOutputMessage} into a {@link WSOutputMessageDTO}.
     * @param wsOutputMessage the {@link WSOutputMessage} to be converted.
     * @return the converted {@link WSOutputMessageDTO}.
     */
    public static WSOutputMessageDTO toDTO(WSOutputMessage wsOutputMessage) {
        return new WSOutputMessageDTO(
                toDTO(wsOutputMessage.getFromUser()),
                wsOutputMessage.getToRoom(),
                wsOutputMessage.getMsg(),
                LocalDate.now(),
                wsOutputMessage.getId().toString(),
                wsOutputMessage.getB64Image());
    }

    /**
     * Converts a {@link UserDTO} into a {@link User}.
     * @param dto the {@link UserDTO} to be converted.
     * @return the converted {@link User}.
     */
    public static User toModel(UserDTO dto) {
        return new User(fromStringToUUID(dto.getId()), dto.getUsername(), dto.getFirstname(), dto.getLastname(),
                dto.getDescription());
    }

    /**
     * Converts a {@link PostDTO} into a {@link Post}.
     * @param dto the {@link PostDTO} to be converted.
     * @return the converted {@link Post}.
     */
    public static Post toModel(PostDTO dto) {
        return new Post(toModel(dto.getCreator()), dto.getBody());
    }

    /**
     * Converts a {@link ChatRoomDTO} into a {@link ChatRoom}.
     * @param chatRoomDTO the {@link ChatRoomDTO} to be converted.
     * @return the converted {@link ChatRoom}.
     */
    public static ChatRoom toModel(ChatRoomDTO chatRoomDTO) {
        return new ChatRoom(chatRoomDTO.getName(), toModel(chatRoomDTO.getOwner()));
    }

    /**
     * Converts a {@link WSIncomingMessageDTO} into a {@link WSIncomingMessage}.
     * @param wsIncomingMessageDTO the {@link WSIncomingMessageDTO} to be converted.
     * @return the converted {@link WSIncomingMessage}.
     */
    public static WSIncomingMessage toModel(WSIncomingMessageDTO wsIncomingMessageDTO) {
        return new WSIncomingMessage(wsIncomingMessageDTO.getFromUser(), wsIncomingMessageDTO.getMsg());
    }

    /**
     * Converts a {@link WSOutputMessageDTO} into a {@link WSOutputMessage}.
     * @param wsOutputMessageDTO the {@link WSOutputMessageDTO} to be converted.
     * @return the converted {@link WSOutputMessage}.
     */
    public static WSOutputMessage toModel(WSOutputMessageDTO wsOutputMessageDTO) {
        return new WSOutputMessage(
                toModel(wsOutputMessageDTO.getFromUser()),
                wsOutputMessageDTO.getToRoom(),
                wsOutputMessageDTO.getMsg(),
                wsOutputMessageDTO.getSentAt());
    }

    /**
     * Creates a UUID from the given string if it matches the standard representation.
     * @param id the id to be converted.
     * @return the UUID if succeeded, null otherwise.
     */
    public static UUID fromStringToUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
