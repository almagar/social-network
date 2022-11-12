package com.example.socialnetwork.security;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.model.User;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class AddUserFilter extends OncePerRequestFilter {
    @Autowired
    private final UserDAO userDAO;

    public AddUserFilter(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof KeycloakPrincipal<?>) {
            KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
            AccessToken token = principal.getKeycloakSecurityContext().getToken();
            UUID id = UUID.fromString(principal.toString());
            String email = token.getEmail();
            String username = token.getPreferredUsername();
            if (!userDAO.existsById(id)) {
                User user = new User(id, email, username);
                userDAO.save(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
