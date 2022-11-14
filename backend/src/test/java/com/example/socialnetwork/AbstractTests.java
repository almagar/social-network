package com.example.socialnetwork;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.example.socialnetwork.dao.PostDAO;
import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@EnableAutoConfiguration
@SpringBootTest(classes = {PostService.class, PostDAO.class, UserService.class})
@ActiveProfiles(profiles = "testing")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockKeycloakAuth(claims =
    @OpenIdClaims(sub = "80b14f39-178f-4068-86a7-358c7a032d4c"))
public abstract class AbstractTests {
    @Autowired
    protected UserService userService;

    private User testUser;

    @Test
    public void createAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
        AccessToken token = principal.getKeycloakSecurityContext().getToken();
        UUID id = UUID.fromString(token.getSubject());
        testUser = new User(id, "email", "username");
        userService.create(testUser);
    }

    protected User getTestUser() {
        return this.testUser;
    }
}
