package com.example.socialnetwork.controller;

import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import com.example.socialnetwork.response.Error;
import com.example.socialnetwork.response.ResponseBuilder;
import com.example.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for the post entity.
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody String body) {
        try {
            postService.create(body);
            return ResponseBuilder.status(HttpStatus.OK).build();
        } catch (RequiredFieldsException ex) {
            return ResponseBuilder.error(Error.REQUIRED_FIELDS).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getPostsByUsername(
            @PathVariable String username, @RequestParam int page, @RequestParam int pageSize) {
        try {
            return ResponseBuilder.data(postService.getPostsByUsername(username, page, pageSize)).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        }
    }

    @GetMapping("/following")
    public ResponseEntity<Map<String, Object>> getPostsByFollowing(@RequestParam int page, @RequestParam int pageSize) {
        try {
            return ResponseBuilder.data(postService.getPostsByFollowing(page, pageSize)).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.USER_NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }
}
