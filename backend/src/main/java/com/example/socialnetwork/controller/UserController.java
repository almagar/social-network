package com.example.socialnetwork.controller;

import com.example.socialnetwork.model.User;
import com.example.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/open")
    public List<String> getOpen() {
        return List.of("open", "not closed");
    }

    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<String> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("User: " + currentPrincipalName);
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Authorities: " + authentication.getAuthorities());
        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("keycloakprincipal");
        System.out.println(keycloakPrincipal);
        System.out.printf(keycloakPrincipal.getKeycloakSecurityContext().getToken().getEmail());
        return List.of("linus", "alma");
        //return userService.getAll();
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
    }
}