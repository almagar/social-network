package com.example.socialnetwork.controller;

import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.IllegalContentTypeException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controller for the post entity.
 */
@RestController
@RequestMapping(path = "/post", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam String body, @RequestParam(required = false) MultipartFile image) {
        try {
            return ResponseBuilder.data(postService.create(body, image)).build();
        } catch (RequiredFieldsException ex) {
            return ResponseBuilder.error(Error.REQUIRED_FIELDS).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        } catch (IllegalContentTypeException ex) {
            return ResponseBuilder.error(Error.INVALID_CONTENT_TYPE).build();
        } catch (Exception ex) {
            return ResponseBuilder.status(HttpStatus.BAD_REQUEST).build();
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

    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> update(@PathVariable String id,
                                                      @RequestParam(required = false) String body,
                                                      @RequestBody(required = false) MultipartFile image) {
        try {
            return ResponseBuilder.data(postService.update(id, body, image)).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.error(Error.RESOURCE_NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        } catch (IllegalContentTypeException ex) {
            return ResponseBuilder.error(Error.INVALID_CONTENT_TYPE).build();
        } catch (Exception ex) {
            return ResponseBuilder.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(path = "/{id}/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Object getImage(@PathVariable String id) {
        try {
            return postService.getImage(id);
        } catch (NotFoundException e) {
            return ResponseBuilder.error(Error.RESOURCE_NOT_FOUND).build();
        }
    }
}
