package com.example.socialnetwork.controller;

import com.example.socialnetwork.model.chat.WSMessage;
import com.example.socialnetwork.model.chat.WSOutputMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping(path = "/ws")
public class WebSocketController {
    @MessageMapping("/chat/{broker}")
    @SendTo("/broker/messages")
    public WSOutputMessage sendOut(@DestinationVariable("broker") String to, WSMessage message) {
        return new WSOutputMessage(message.getFrom(), to, message.getText(), LocalDate.now());
    }
}
