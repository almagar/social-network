package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.WSIncomingMessageDTO;
import com.example.socialnetwork.dto.WSOutputMessageDTO;
import com.example.socialnetwork.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class WebSocketController {
    @Autowired
    private final WebSocketService webSocketService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/broker/chat/{roomId}")
    public WSOutputMessageDTO sendOut(@DestinationVariable("roomId") String roomId, WSIncomingMessageDTO message, SimpMessageHeaderAccessor accessor) {
        System.out.println("sending out");
        return webSocketService.handleMessage(roomId, accessor.getUser().toString(), message);
    }
}
