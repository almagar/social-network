package com.example.socialnetwork;

import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostServiceTests extends AbstractTests {
    @Autowired
    private PostService postService;

    @Test
    public void testCreatePostAndFindById() {
        PostDTO created = postService.create("testing");
        PostDTO found = postService.findById(created.getId());
        Assertions.assertEquals(created.getId(), found.getId());
    }
}
