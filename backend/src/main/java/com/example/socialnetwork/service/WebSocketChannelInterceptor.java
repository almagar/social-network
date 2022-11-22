package com.example.socialnetwork.service;

import com.example.socialnetwork.dto.WSIncomingMessageDTO;
import com.example.socialnetwork.model.exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private final KeycloakSpringBootProperties keycloakSpringBootProperties;

    private final ChatRoomService chatRoomService;

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null)
            return null;

        switch (accessor.getCommand()) {
            case CONNECT -> handleConnect(accessor);
            case DISCONNECT -> handleDisconnect(accessor);
            case SUBSCRIBE -> handleSubscribe(accessor, channel);
            case SEND -> handleSend(accessor, (Message<WSIncomingMessageDTO>) message);
            default -> System.out.println("-------------- unhandled command: " + accessor.getCommand() + " -------------------");
        }
        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        AccessToken token = getToken(accessor);
        KeycloakSecurityContext keycloakSecurityContext= new KeycloakSecurityContext(getTokenStr(accessor), token, "id", new IDToken());
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = new KeycloakPrincipal<>(token.getSubject(), keycloakSecurityContext);
        accessor.setUser(keycloakPrincipal);
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        System.out.println("disconnect");
    }

    private void handleSubscribe(StompHeaderAccessor accessor, MessageChannel channel) {
        // set accessor user
        AccessToken token = getToken(accessor);
        KeycloakSecurityContext keycloakSecurityContext= new KeycloakSecurityContext(getTokenStr(accessor), token, "id", new IDToken());
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = new KeycloakPrincipal<>(token.getSubject(), keycloakSecurityContext);
        accessor.setUser(keycloakPrincipal);

        // TODO: send out last 10 sent messages
        //Message<> message =
        //channel.send();
    }

    private void handleSend(StompHeaderAccessor accessor, Message<WSIncomingMessageDTO> message) {
        AccessToken token = getToken(accessor);
        KeycloakSecurityContext keycloakSecurityContext= new KeycloakSecurityContext(getTokenStr(accessor), token, "id", new IDToken());
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = new KeycloakPrincipal<>(token.getSubject(), keycloakSecurityContext);
        accessor.setUser(keycloakPrincipal);
        accessor.updateStompCommandAsServerMessage();
    }

    private AccessToken getToken(StompHeaderAccessor accessor) {
        try {
            String token = getTokenStr(accessor);
            return AdapterTokenVerifier.verifyToken(token, KeycloakDeploymentBuilder.build(keycloakSpringBootProperties));
        } catch (VerificationException e) {
            System.out.println("VerificationException: ");
            System.out.println(e);
            throw new AuthorizationException();
        }
    }

    private String getTokenStr(StompHeaderAccessor accessor) {
        List<String> headers = accessor.getNativeHeader("Authorization");
        System.out.println(headers);
        if (headers == null || headers.size() <= 0)
            throw new AuthorizationException();
        return headers.get(0).replace("Bearer ", "");
    }
}
