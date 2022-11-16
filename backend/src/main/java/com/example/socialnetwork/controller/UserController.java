package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.response.Error;
import com.example.socialnetwork.response.ResponseBuilder;
import com.example.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        try {
            UserDTO user = userService.getProfile();
            return ResponseBuilder.data(user).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping("/{username}")
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

    @PutMapping("/follow/{id}")
    public ResponseEntity<Map<String, Object>> followUser(@PathVariable String id) {
        try {
            userService.followUser(id);
            return ResponseBuilder.status(HttpStatus.OK).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        } catch (IllegalStateException ex) {
            return ResponseBuilder.error(Error.ALREADY_FOLLOWING).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @PutMapping("/unfollow/{id}")
    public ResponseEntity<Map<String, Object>> unfollowUser(@PathVariable String id) {
        try {
            userService.unFollowUser(id);
            return ResponseBuilder.status(HttpStatus.OK).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        } catch (IllegalStateException ex) {
            return ResponseBuilder.error(Error.NOT_FOLLOWING).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping("/following")
    public ResponseEntity<Map<String, Object>> getFollowingList() {
        try {
            Collection<UserDTO> friends = userService.getFollowingList();
            return ResponseBuilder.data(friends).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<Map<String, Object>> getFollowingList(@PathVariable String id) {
        try {
            Collection<UserDTO> friends = userService.getFollowingList(id);
            return ResponseBuilder.data(friends).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        }
    }
}