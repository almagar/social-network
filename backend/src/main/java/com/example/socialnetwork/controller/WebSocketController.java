package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.WSIncomingMessageDTO;
import com.example.socialnetwork.dto.WSOutputMessageDTO;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.AuthorizationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.response.Error;
import com.example.socialnetwork.response.ResponseBuilder;
import com.example.socialnetwork.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private final WebSocketService webSocketService;

    @MessageMapping("/chat/{roomId}")
    //@SendTo("/chat/{roomId}")
    public void sendOut(@DestinationVariable("roomId") String roomId, WSIncomingMessageDTO message) {
        System.out.println("ws controller: " + message);
        System.out.println("roomId: " + roomId);
        try {
            WSOutputMessageDTO outputMessage = webSocketService.validateSenderAndMessage(roomId, message);
            simpMessagingTemplate.convertAndSend("/chat/" + roomId, outputMessage);
        } catch (NotFoundException ex) {
            //return ResponseBuilder.status(HttpStatus.NOT_FOUND).error(Error.CHATROOM_NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            //return ResponseBuilder.status(HttpStatus.UNAUTHORIZED).error(Error.AUTHENTICATION_ERROR).build();
        } catch (AuthorizationException ex) {
            //return ResponseBuilder.status(HttpStatus.FORBIDDEN).error(Error.AUTHORIZATION_ERROR).build();
        }
    }

    @SubscribeMapping("/chat/{roomId}")
    public ResponseEntity<Map<String, Object>> subscribe(@DestinationVariable("roomId") String roomId) {
        System.out.println("subscribe to: " + roomId);
        try {
            if (!webSocketService.canSubscribeToRoom(roomId))
                throw new AuthorizationException();
            return ResponseBuilder.status(HttpStatus.OK).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.status(HttpStatus.UNAUTHORIZED).error(Error.AUTHENTICATION_ERROR).build();
        } catch (AuthorizationException ex) {
            return ResponseBuilder.status(HttpStatus.FORBIDDEN).error(Error.AUTHORIZATION_ERROR).build();
        }
    }
}
