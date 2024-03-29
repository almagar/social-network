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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UserServiceTests extends AbstractTests {
    @Autowired
    private UserService userService;

    @BeforeEach
    private void addTemporaryUser() {
        userService.create(getExampleUser());
    }

    @Test
    public void testUpdateUser() {
        String newDescription = "new description";
        try {
            userService.update(newDescription, null);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        Assertions.assertEquals(newDescription, userService.getProfile().getDescription());
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
                Mapper.toDTO(getLoggedInUser()), Mapper.toDTO(getExampleUser())), userService.getAll());
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
    public void testFollow() {
        userService.followUser(getExampleUser().getId().toString());
        Assertions.assertEquals(1, userService.getFollowing().size());
    }

    @Test
    public void testUnfollow() {
        User tmpUser = getExampleUser();
        userService.followUser(tmpUser.getId().toString());
        userService.unFollowUser(tmpUser.getId().toString());
        Assertions.assertEquals(Collections.emptyList(), userService.getFollowing());
    }

    @Test
    public void testDeleteUser() {
        User tmpUser = getExampleUser();
        userService.delete(tmpUser);
        Assertions.assertFalse(userService.exists(getExampleUser().getId()));
    }

    protected static User getExampleUser() {
        return new User(UUID.fromString(
                "70b14f39-178f-4068-86a7-358c7a032d4c"), "email@example.com", "example",
                "firstname", "lastname");
    }

    protected static User getRandomUser() {
        int randomInt = new Random().nextInt(1000);
        return new User(UUID.randomUUID(), randomInt + "@example.com", "user" + randomInt,
                "firstname", "lastname");
    }
}
