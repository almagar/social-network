package com.example.socialnetwork.controller;

import com.example.socialnetwork.model.User;
import com.example.socialnetwork.response.ResponseBuilder;
import com.example.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired private final UserService userService;

    @GetMapping("/open")
    public List<String> getOpen() {
        return List.of("open", "not closed");
    }

    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("User: " + currentPrincipalName);
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Authorities: " + authentication.getAuthorities());

        return ResponseBuilder.data("hej").build();
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
    }
}