package com.example.socialnetwork.dao;

import com.example.socialnetwork.model.Post;
import com.example.socialnetwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

/**
 * Data access object for the {@link Post} entity.
 */
@Repository
public interface PostDAO extends GenericDAO<Post, UUID> {
    /**
     * Retrieves a page of {@link Post}s created by the given {@link User} id.
     * @param id the id of the {@link User}.
     * @param pageable the pagination information.
     * @return a page of {@link Post}s.
     */
    Page<Post> getAllByUserId(UUID id, Pageable pageable);

    /**
     * Retrieves a page of {@link Post}s created by the given {@link User}s.
     * @param users the {@link Set} of {@link User}s.
     * @param pageable the pagination information.
     * @return a page of {@link Post}s.
     */
    @Query(value = "select * from post where post.user_id in :users", nativeQuery = true)
    Page<Post> getAllByUsers(Set users, Pageable pageable);
}
