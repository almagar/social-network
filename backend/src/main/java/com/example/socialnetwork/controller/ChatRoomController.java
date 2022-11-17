package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.ChatRoomDTO;
import com.example.socialnetwork.model.exception.AuthenticationException;
import com.example.socialnetwork.model.exception.AuthorizationException;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import com.example.socialnetwork.response.Error;
import com.example.socialnetwork.response.ResponseBuilder;
import com.example.socialnetwork.service.ChatRoomService;
import com.example.socialnetwork.service.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/chatroom", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getJoinedRooms() {
        try {
            List<ChatRoomDTO> chatRooms = chatRoomService.getJoined();
            return ResponseBuilder.data(chatRooms).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.status(HttpStatus.UNAUTHORIZED).error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @GetMapping("/owned")
    public ResponseEntity<Map<String, Object>> getOwnedRooms() {
        try {
            List<ChatRoomDTO> chatRooms = chatRoomService.getOwned();
            return ResponseBuilder.data(chatRooms).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.status(HttpStatus.UNAUTHORIZED).error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody String name) {
        try {
            ChatRoomDTO chatRoom = chatRoomService.create(name);
            return ResponseBuilder.status(HttpStatus.CREATED).data(chatRoom).build();
        } catch (RequiredFieldsException ex) {
            return ResponseBuilder.status(HttpStatus.BAD_REQUEST).error(Error.REQUIRED_FIELDS).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.status(HttpStatus.UNAUTHORIZED).error(Error.AUTHENTICATION_ERROR).build();
        }
    }

    // TODO: check if @RequestBody actually works here
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addUserToRoom(@RequestBody String chatRoomId, @RequestBody String userId) {
        try {
            ChatRoomDTO chatRoomDTO = chatRoomService.addUserToRoom(Mapper.fromStringToUUID(chatRoomId), Mapper.fromStringToUUID(userId));
            return ResponseBuilder.data(chatRoomDTO).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.status(HttpStatus.NOT_FOUND).error(Error.CHATROOM_NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.status(HttpStatus.UNAUTHORIZED).error(Error.AUTHENTICATION_ERROR).build();
        } catch (AuthorizationException ex) {
            return ResponseBuilder.status(HttpStatus.FORBIDDEN).error(Error.AUTHORIZATION_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String id) {
        try {
            chatRoomService.delete(Mapper.fromStringToUUID(id));
            return ResponseBuilder.status(HttpStatus.OK).build();
        } catch (NotFoundException ex) {
            return ResponseBuilder.status(HttpStatus.NOT_FOUND).error(Error.CHATROOM_NOT_FOUND).build();
        } catch (AuthenticationException ex) {
            return ResponseBuilder.status(HttpStatus.UNAUTHORIZED).error(Error.AUTHENTICATION_ERROR).build();
        } catch (AuthorizationException ex) {
            return ResponseBuilder.status(HttpStatus.FORBIDDEN).error(Error.AUTHORIZATION_ERROR).build();
        }
    }
}
