package com.example.socialnetwork.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;
import java.util.UUID;

/**
 * User entity that is also mapped to a database.
 */
@Entity(name = "Users")
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

    public User(UUID id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
}
