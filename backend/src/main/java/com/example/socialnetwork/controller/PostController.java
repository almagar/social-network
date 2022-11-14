package com.example.socialnetwork.controller;

import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import com.example.socialnetwork.response.Error;
import com.example.socialnetwork.response.ResponseBuilder;
import com.example.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller for the post entity.
 */
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(params = {"body"})
    public ResponseEntity<Map<String, Object>> create(@RequestParam String body) {
        try {
            postService.create(body);
            return ResponseBuilder.status(HttpStatus.OK).build();
        } catch (RequiredFieldsException ex) {
            return ResponseBuilder.error(Error.REQUIRED_FIELDS).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.error(Error.AUTHENTICATION_ERROR).build();
        }
    }
}
