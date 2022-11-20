package com.example.socialnetwork.config;

import com.example.socialnetwork.security.KeycloakHttpSessionHandshakeInterceptor;
import com.example.socialnetwork.service.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private final KeycloakHttpSessionHandshakeInterceptor keycloakHttpSessionHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/broker");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .addInterceptors(keycloakHttpSessionHandshakeInterceptor)
                .setAllowedOrigins("http://localhost:8080", "http://localhost:8282", "http://localhost:3000")
                .withSockJS();
    }
}
