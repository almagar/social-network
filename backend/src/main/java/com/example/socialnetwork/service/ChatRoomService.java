package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.ChatRoomDAO;
import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.ChatRoomDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.chat.ChatRoom;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.AuthorizationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class ChatRoomService extends AbstractService {
    private final ChatRoomDAO chatRoomDAO;

    /**
     * Constructor for {@link ChatRoomService}.
     * @param userDAO {@link UserDAO}.
     * @param chatRoomDAO {@link ChatRoomDAO}.
     */
    public ChatRoomService(UserDAO userDAO, ChatRoomDAO chatRoomDAO) {
        super(userDAO);
        this.chatRoomDAO = chatRoomDAO;
    }

    /**
     * Create a new chatroom.
     * @param name name of the chatroom.
     * @throws AuthenticationException if the user trying to create the room is not authenticated.
     */
    public ChatRoomDTO create(String name) throws RequiredFieldsException, AuthenticationException {
        if (name == null || "".equals(name))
            throw new RequiredFieldsException();
        User user = getLoggedInUser();
        ChatRoom chatRoom = new ChatRoom(name, user);
        chatRoom.addUserToRoom(user);
        return Mapper.toDTO(chatRoomDAO.save(chatRoom));
    }

    /**
     * Delete a chatroom, only allowed by the owner of the chatroom.
     * @param chatRoomId the id of the chatroom to delete.
     * @throws AuthenticationException if the user is not logged in.
     * @throws AuthorizationException if the user is not the owner of the chatroom.
     */
    public void delete(UUID chatRoomId) throws NotFoundException, AuthenticationException, AuthorizationException {
        ChatRoom chatRoom = chatRoomDAO.findById(chatRoomId).orElseThrow(NotFoundException::new);
        User user = getLoggedInUser();
        if (!user.getId().equals(chatRoom.getOwner().getId())) {
            throw new AuthorizationException();
        }
        chatRoomDAO.delete(chatRoom);
    }

    /**
     * Check if the specified chatroom exists.
     * @param id id of the chatroom to check for.
     * @return true if it exists, false otherwise.
     */
    public boolean exists(UUID id) {
        return chatRoomDAO.existsById(id);
    }

    /**
     * Get chatrooms that the logged-in user owns.
     * @return a {@link List<ChatRoomDTO>} of the chatrooms that the user owns.
     * @throws AuthenticationException if the user is not logged in.
     */
    public List<ChatRoomDTO> getOwned() throws AuthenticationException {
        return chatRoomDAO.findByOwnerOrderByName(getLoggedInUser()).stream().map(Mapper::toDTO).toList();
    }

    /**
     * Get chatroom by id.
     * @param id the {@link UUID} of the room to get.
     * @return the found {@link ChatRoomDTO}.
     * @throws NotFoundException if the chatroom is not found.
     * @throws AuthenticationException if the user is not logged in.
     * @throws AuthorizationException if the user trying to get the chatroom is not allowed to.
     */
    public ChatRoomDTO getById(String id) throws NotFoundException, AuthenticationException, AuthorizationException {
        if (!isInRoom(Mapper.fromStringToUUID(id)))
            throw new AuthorizationException();
        return Mapper.toDTO(chatRoomDAO.findById(Mapper.fromStringToUUID(id)).orElseThrow(NotFoundException::new));
    }

    /**
     * Get chatrooms that the user is in.
     * @return a {@link List<ChatRoomDTO>} of the chatrooms that the user is in.
     * @throws AuthenticationException if the user is not logged in.
     */
    public List<ChatRoomDTO> getJoined() throws AuthenticationException {
        return chatRoomDAO.findByUsersContainingOrderByName(getLoggedInUser()).stream().map(Mapper::toDTO).toList();
    }

    /**
     * Check if the logged-in user is in the room specified by roomId.
     * @param roomId the id of the room.
     * @return true if the user is in the room, false otherwise.
     * @throws AuthenticationException if the user is not logged in.
     */
    public boolean isInRoom(UUID roomId) throws AuthenticationException {
        User user = getLoggedInUser();
        System.out.println("user:");
        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getChatRooms());
        return chatRoomDAO
                .findByUsersContainingOrderByName(getLoggedInUser())
                .stream()
                .map(ChatRoom::getId)
                .anyMatch(Predicate.isEqual(roomId));
    }

    /**
     * Check if the user specified by userId is in the room specified by roomId.
     * @param roomId the id of the room.
     * @param userId the id of the user.
     * @return true if the user is in the room, false otherwise.
     * @throws NotFoundException if either the roomId or the userId is not found.
     */
    public boolean isInRoom(UUID roomId, UUID userId) throws NotFoundException {
        User user = userDAO.findById(userId).orElseThrow(NotFoundException::new);
        System.out.println("user:");
        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getChatRooms());
        return chatRoomDAO
                .findByUsersContainingOrderByName(user)
                .stream()
                .map(ChatRoom::getId)
                .anyMatch(Predicate.isEqual(roomId));
    }

    /**
     * Add a user to a chatroom
     * @param chatRoomId id of the chatroom to add the user to.
     * @param userId id of the user to add.
     * @throws NotFoundException if either the chatroom or the user is not found.
     * @throws AuthenticationException if the user trying to add the other user is not logged in.
     * @throws AuthorizationException if the user trying to add the other user is not the owner of the chat.
     */
    public ChatRoomDTO addUserToRoom(UUID chatRoomId, UUID userId) throws NotFoundException, AuthenticationException, AuthorizationException {
        User loggedInUser = getLoggedInUser();
        ChatRoom chatRoom = chatRoomDAO.findById(chatRoomId).orElseThrow(NotFoundException::new);
        User user = userDAO.findById(userId).orElseThrow(NotFoundException::new);
        if (!loggedInUser.getId().equals(chatRoom.getOwner().getId()))
            throw new AuthorizationException();
        chatRoom.addUserToRoom(user);
        return Mapper.toDTO(chatRoomDAO.save(chatRoom));
    }
}
