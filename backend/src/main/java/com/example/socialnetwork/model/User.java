package com.example.socialnetwork.model;

import lombok.Getter;
import com.example.socialnetwork.model.chat.ChatRoom;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;
import java.util.UUID;

/**
 * User entity that is also mapped to a database.
 */
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] avatar;

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

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<ChatRoom> chatRooms;

    public User(UUID id, String email, String username, String firstname, String lastname) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
