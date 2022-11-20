package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.WSIncomingMessageDTO;
import com.example.socialnetwork.dto.WSOutputMessageDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.chat.WSIncomingMessage;
import com.example.socialnetwork.model.chat.WSOutputMessage;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.AuthorizationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    @Autowired
    private final UserService userService;

    @Autowired
    private final ChatRoomService chatRoomService;

    public WSOutputMessageDTO validateSenderAndMessage(String roomId, WSIncomingMessageDTO incomingMessage) throws NotFoundException, AuthenticationException, AuthorizationException {
        WSIncomingMessage wsIncomingMessage = Mapper.toModel(incomingMessage);
        User user = Mapper.toModel(userService.getByUsername(wsIncomingMessage.getFromUser()));
        if (!chatRoomService.exists(Mapper.fromStringToUUID(roomId)))
            throw new NotFoundException();

        if (!chatRoomService.isInRoom(Mapper.fromStringToUUID(roomId)))
            throw new AuthorizationException();

        WSOutputMessage wsOutputMessage = new WSOutputMessage(user, roomId, wsIncomingMessage.getMsg(), LocalDate.now());
        return Mapper.toDTO(wsOutputMessage);
    }

    public boolean canSubscribeToRoom(String roomId) throws AuthenticationException {
        return chatRoomService.isInRoom(Mapper.fromStringToUUID(roomId));
    }
}
