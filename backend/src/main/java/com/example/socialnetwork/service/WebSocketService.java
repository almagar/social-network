package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.ChatRoomDAO;
import com.example.socialnetwork.dto.WSIncomingMessageDTO;
import com.example.socialnetwork.dto.WSOutputMessageDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.chat.ChatRoom;
import com.example.socialnetwork.model.chat.WSIncomingMessage;
import com.example.socialnetwork.model.chat.WSOutputMessage;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.AuthorizationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    @Autowired
    private final UserService userService;

    @Autowired
    private final ChatRoomService chatRoomService;

    @Autowired
    private final ChatRoomDAO chatRoomDAO;

    public WSOutputMessageDTO handleMessage(String roomId, String userId, WSIncomingMessageDTO incomingMessage) throws NotFoundException, AuthenticationException, AuthorizationException {
        WSIncomingMessage wsIncomingMessage = Mapper.toModel(incomingMessage);
        User user = Mapper.toModel(userService.getById(Mapper.fromStringToUUID(userId)));
        if (!chatRoomService.exists(Mapper.fromStringToUUID(roomId)))
            throw new NotFoundException();

        if (!chatRoomService.isInRoom(Mapper.fromStringToUUID(roomId), user.getId()))
            throw new AuthorizationException();

        WSOutputMessage wsOutputMessage = new WSOutputMessage(user, roomId, wsIncomingMessage.getMsg(), LocalDate.now(), incomingMessage.getB64Image());

        //persistMessage(Mapper.fromStringToUUID(roomId), wsOutputMessage);

        return Mapper.toDTO(wsOutputMessage);
    }

    public boolean canSubscribeToRoom(String roomId, String userId) throws NotFoundException {
        return chatRoomService.isInRoom(Mapper.fromStringToUUID(roomId), Mapper.fromStringToUUID(userId));
    }

    private void persistMessage(UUID roomId, WSOutputMessage message) throws NotFoundException {
        ChatRoom chatRoom = chatRoomDAO.findById(roomId).orElseThrow(NotFoundException::new);
        chatRoom.addMessage(message);
        chatRoomDAO.save(chatRoom);
    }
}
