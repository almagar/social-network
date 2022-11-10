package com.example.socialnetwork.security;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.model.User;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

public class AddUserFilter extends GenericFilterBean {
    @Autowired
    private UserDAO userDAO;

    public AddUserFilter(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
            AccessToken token = principal.getKeycloakSecurityContext().getToken();
            UUID id = UUID.fromString(principal.toString());
            String email = token.getEmail();
            String username = token.getPreferredUsername();
            if (!userDAO.existsById(id)) {
                User user = new User(id, email, username);
                userDAO.save(user);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            throw new RuntimeException();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
