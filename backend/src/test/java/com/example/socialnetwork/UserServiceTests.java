package com.example.socialnetwork;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.service.Mapper;
import com.example.socialnetwork.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EnableAutoConfiguration
@SpringBootTest(classes = {UserService.class, UserDAO.class})
@ActiveProfiles(profiles = "testing")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @BeforeAll
    public void setup() {
        userService.createUser(getTestUser());
    }

    @Test
    public void testUserExists() {
        Assertions.assertTrue(userService.userExists(getTestUser().getId()));
        Assertions.assertFalse(userService.userExists(UUID.randomUUID()));
    }

    @Test
    public void testGetAll() {
        Assertions.assertEquals(List.of(Mapper.toDTO(getTestUser())), userService.getAll());
    }

    @Test
    public void testGetByUsername() {
        String name = getTestUser().getUsername();
        UserDTO userDTO = userService.getByUsername(name);

        Assertions.assertEquals(name, userDTO.getUsername());
        Assertions.assertThrows(NotFoundException.class, () -> userService.getByUsername("non_existing"));
    }

    @Test
    public void testSearchByUsername() {
        Assertions.assertEquals(List.of(Mapper.toDTO(getTestUser())), userService.searchByUsername("test"));
        Assertions.assertEquals(Collections.emptyList(), userService.searchByUsername("test_x"));
    }

    private static User getTestUser() {
        return new User(UUID.fromString("9728bbd4-2a7e-4cef-aa9e-8da4e2b21ed9"), "test@test.com", "testuser");
    }
}
