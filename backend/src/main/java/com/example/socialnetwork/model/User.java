package com.example.socialnetwork.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;
import java.util.*;

/**
 * User entity that is also mapped to a database.
 */
@Entity(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String description;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private Blob avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "follow",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<User> following = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "follow",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> followers = new HashSet<>();

    public User(UUID id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    /**
     * Adds the given {@link User} to this {@link User}'s following list.
     * @param user the {@link User}.
     */
    public void follow(User user) {
        following.add(user);
    }

    /**
     * Removes the given {@link User} from this {@link User}'s following list.
     * @param user the {@link User}.
     */
    public void unfollow(User user) {
        following.remove(user);
    }
}
