package com.example.socialnetwork;

import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import com.example.socialnetwork.model.exception.NotFoundException;
import com.example.socialnetwork.model.exception.RequiredFieldsException;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PostServiceTests extends AbstractTests {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Test
    public void testCreateNullPostThrowsException() {
        Assertions.assertThrows(RequiredFieldsException.class, () -> postService.create(null, (MultipartFile) null));
    }

    @Test
    public void testGetPostsByUsername() throws IOException {
        PostDTO dto = postService.create("testing", (MultipartFile) null);
        List<PostDTO> dtos = (List<PostDTO>) postService.getPostsByUsername(
                getLoggedInUser().getUsername(), 0, 1);
        Assertions.assertEquals(dto.getId(), dtos.get(0).getId());
    }

    @Test
    public void testGetPostsByNonExistingUsernameThrowsException() {
        Assertions.assertThrows(
                NotFoundException.class, () -> postService.getPostsByUsername("non_existing", 0, 1));
    }

    @Test
    public void testGetPostsByFollowing() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = UserServiceTests.getRandomUser();
            users.add(user);
            userService.create(user);
        }

        List<Post> posts = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Post post = postService.create("testing", users.get(r.nextInt(3)));
            posts.add(post);
        }

        for (User user : users) {
            userService.followUser(user.getId().toString());
        }

        Assertions.assertEquals(posts.stream().map(
                        post -> post.getCreatedAt().truncatedTo(ChronoUnit.MILLIS)).toList(),
                        postService.getPostsByFollowing(0, 10).stream().map(
                        post -> post.getCreatedAt().truncatedTo(ChronoUnit.MILLIS)).toList());
    }
}
