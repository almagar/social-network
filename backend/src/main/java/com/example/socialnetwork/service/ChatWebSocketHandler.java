package com.example.socialnetwork.service;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
public class ChatWebSocketHandler extends AbstractWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession wsSession) throws Exception {
        System.out.println("established:" + wsSession);
        super.afterConnectionEstablished(wsSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) throws Exception {
        System.out.println("closed:" + wsSession);
        super.afterConnectionClosed(wsSession, status);
    }

    @Override
    public void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
        System.out.println("text message s:" + wsSession);
        System.out.println("message:" + message);
        super.handleTextMessage(wsSession, message);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession wsSession, BinaryMessage message) throws Exception {
        System.out.println("binary message s:" + wsSession);
        System.out.println("message:" + message);
        super.handleBinaryMessage(wsSession, message);
    }
}
