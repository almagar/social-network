package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Abstract service class.
 */
@RequiredArgsConstructor
abstract class AbstractService {
    protected final UserDAO userDAO;

    protected User getLoggedInUser() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
            AccessToken token = principal.getKeycloakSecurityContext().getToken();
            UUID id = Mapper.fromStringToUUID(token.getSubject());
            Optional<User> found =
                    userDAO.findById(id);
            if (found.isEmpty()) {
                throw new NotFoundException();
            }

            return found.get();
        } catch (Exception ex) {
            throw new AuthenticationException();
        }
    }
}
