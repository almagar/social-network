package com.example.socialnetwork;

import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.service.Mapper;
import com.example.socialnetwork.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserServiceTests extends AbstractTests {
    @Autowired
    private UserService userService;

    @BeforeEach
    private void addTemporaryUser() {
        userService.create(getTemporaryUser());
    }

    @Test
    public void testUserExists() {
        Assertions.assertTrue(userService.exists(getLoggedInUser().getId()));
    }

    @Test
    public void testUserNotExists() {
        Assertions.assertFalse(userService.exists(UUID.randomUUID()));
    }

    @Test
    public void testGetAll() {
        Assertions.assertEquals(List.of(
                Mapper.toDTO(getLoggedInUser()), Mapper.toDTO(getTemporaryUser())), userService.getAll());
    }

    @Test
    public void testGetByUsername() {
        String name = getLoggedInUser().getUsername();
        UserDTO userDTO = userService.getByUsername(name);

        Assertions.assertEquals(name, userDTO.getUsername());
    }

    @Test
    public void testGetByNonExistingUsernameThrowsException() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.getByUsername("non_existing"));
    }

    @Test
    public void testSearchByUsername() {
        Assertions.assertEquals(List.of(Mapper.toDTO(getLoggedInUser())),
                userService.searchByUsername(getLoggedInUser().getUsername()));
    }

    @Test
    public void testSearchByNonExistingUsername() {
        Assertions.assertEquals(Collections.emptyList(),
                userService.searchByUsername(getLoggedInUser().getUsername() + "x"));
    }

    @Test
    public void testFollowUnFollowAndGetFollowingList() {
        User tmpUser = getTemporaryUser();
        userService.followUser(tmpUser.getId().toString());
        Assertions.assertEquals(List.of(Mapper.toDTO(tmpUser)), userService.getFollowingList());
        Assertions.assertEquals(List.of(Mapper.toDTO(tmpUser)),
                userService.getFollowingList(getLoggedInUser().getId().toString()));
    }

    @Test
    public void testUnfollow() {
        User tmpUser = getTemporaryUser();
        userService.followUser(tmpUser.getId().toString());
        userService.unFollowUser(tmpUser.getId().toString());
        Assertions.assertEquals(Collections.emptyList(), userService.getFollowingList());
    }

    @Test
    public void testDeleteUser() {
        User tmpUser = getTemporaryUser();
        userService.delete(tmpUser);
        Assertions.assertFalse(userService.exists(getTemporaryUser().getId()));
    }

    private static User getTemporaryUser() {
        return new User(UUID.fromString(
                "70b14f39-178f-4068-86a7-358c7a032d4c"), "email@example.com", "example");
    }
}
