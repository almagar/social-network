package com.example.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;
import java.util.UUID;

@Entity(name = "Users")
@Data
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
    @JsonIgnore
    @Type(type = "org.hibernate.type.BinaryType")
    private Blob avatar;

    public User() {}

    public User(UUID id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
}
