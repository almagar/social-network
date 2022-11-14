package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.response.Error;
import com.example.socialnetwork.response.ResponseBuilder;
import com.example.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for the user entity.
 */
@RestController
@RequestMapping(path = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/{username}")
    public ResponseEntity<Map<String, Object>> getByUsername(@PathVariable String username) {
        try {
            UserDTO user = userService.getByUsername(username);
            return ResponseBuilder.data(user).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        }
    }

    @GetMapping(params = {"username"})
    public ResponseEntity<Map<String, Object>> searchByUsername(@RequestParam String username) {
        List<UserDTO> users = userService.searchByUsername(username);
        return ResponseBuilder.data(users).build();
    }

    @GetMapping(path = "/{id}/avatar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Map<String, Object>> getAvatar(@PathVariable String id) {
        return null;
    }
}