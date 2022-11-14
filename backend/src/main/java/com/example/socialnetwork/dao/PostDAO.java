package com.example.socialnetwork.dao;

import com.example.socialnetwork.model.Post;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Data access object for the {@link Post} entity.
 */
@Repository
public interface PostDAO extends GenericDAO<Post, UUID> {
}
