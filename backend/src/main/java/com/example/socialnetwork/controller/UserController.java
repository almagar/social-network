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
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping(path = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> update(
            @RequestParam(required = false) String description, @RequestParam(required = false)MultipartFile avatar) {
        try {
            return ResponseBuilder.data(userService.update(description, avatar)).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        } catch (Exception ex) {
            return ResponseBuilder.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(path = "/avatar", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Object getAvatar() {
        try {
            return userService.getAvatar();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping(path = "/{id}/avatar", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Object getAvatar(@PathVariable String id) {
        try {
            return userService.getAvatar(id);
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
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

    @PutMapping("/{id}/follow")
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

    @PutMapping("/{id}/unfollow")
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
    public ResponseEntity<Map<String, Object>> getFollowing() {
        try {
            Collection<UserDTO> following = userService.getFollowing();
            return ResponseBuilder.data(following).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<Map<String, Object>> getFollowing(@PathVariable String id) {
        try {
            Collection<UserDTO> following = userService.getFollowing(id);
            return ResponseBuilder.data(following).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        }
    }

    @GetMapping("/followers")
    public ResponseEntity<Map<String, Object>> getFollowers() {
        try {
            Collection<UserDTO> followers = userService.getFollowers();
            return ResponseBuilder.data(followers).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<Map<String, Object>> getFollowers(@PathVariable String id) {
        try {
            Collection<UserDTO> followers = userService.getFollowers(id);
            return ResponseBuilder.data(followers).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/is-following")
    public ResponseEntity<Map<String, Object>> isFollowing(@PathVariable String id) {
        try {
            return ResponseBuilder.data(userService.isFollowing(id)).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }
}