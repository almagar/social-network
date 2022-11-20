package com.example.socialnetwork.config;

import org.keycloak.KeycloakPrincipal;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpTypeMatchers(
                SimpMessageType.CONNECT,
                SimpMessageType.CONNECT_ACK,
                SimpMessageType.SUBSCRIBE,
                SimpMessageType.MESSAGE,
                SimpMessageType.UNSUBSCRIBE,
                SimpMessageType.DISCONNECT,
                SimpMessageType.DISCONNECT_ACK,
                SimpMessageType.HEARTBEAT)
                .permitAll()
                .simpDestMatchers("/app/**", "/to/**")
                .authenticated()
                .simpSubscribeDestMatchers("/to/**")
                .authenticated()
                .anyMessage()
                .denyAll();
    }

    //@Override
    //public SecurityContextChannelInterceptor securityContextChannelInterceptor() {
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    if (authentication.getPrincipal() instanceof KeycloakPrincipal<?>) {
    //        System.out.println("(security) instance of KeycloakPrincipal");
    //        KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
    //    }
    //    return super.securityContextChannelInterceptor();
    //}

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
