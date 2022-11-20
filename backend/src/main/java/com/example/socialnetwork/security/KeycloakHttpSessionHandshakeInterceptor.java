package com.example.socialnetwork.security;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
public class KeycloakHttpSessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("before handshake");
        System.out.println(attributes);
        KeycloakAuthenticationToken authenticationToken = (KeycloakAuthenticationToken) request.getPrincipal();
        if (request.getPrincipal() instanceof KeycloakPrincipal<?>) {
            System.out.println("instance of KeycloakPrincipal");
        }
        if (request.getPrincipal() instanceof KeycloakAuthenticationToken) {
            System.out.println("instanceof KeycloakAuthenticationToken");
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        System.out.println("after handshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
