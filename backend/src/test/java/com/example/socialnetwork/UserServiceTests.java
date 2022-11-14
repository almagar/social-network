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

public class UserServiceTests extends AbstractTests {
    @Autowired
    private UserService userService;

    @Test
    public void testUserExists() {
        Assertions.assertTrue(userService.exists(getTestUser().getId()));
        Assertions.assertFalse(userService.exists(UUID.randomUUID()));
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
        Assertions.assertEquals(List.of(Mapper.toDTO(getTestUser())),
                userService.searchByUsername(getTestUser().getUsername()));
        Assertions.assertEquals(Collections.emptyList(),
                userService.searchByUsername(getTestUser().getUsername()) + "x");
    }
}
