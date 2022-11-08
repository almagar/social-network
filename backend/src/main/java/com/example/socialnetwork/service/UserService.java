package com.example.socialnetwork.service;

import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    public List<User> getAll() {
        return userDAO.findAll().stream().toList();
    }

    public void createUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().equals("") || user.getUsername() == null || user.getUsername().equals("") || user.getPassword() == null || user.getPassword().equals("")) {
            return;
        }
        // make sure that id is not set, will be generated
        user.setId(null);

        userDAO.save(user);
    }
}
