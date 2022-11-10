package com.example.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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
    @Column(columnDefinition = "bytea")
    private Blob avatar;
}
