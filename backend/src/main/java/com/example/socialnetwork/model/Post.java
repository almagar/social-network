package com.example.socialnetwork.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Post entity that is also mapped to a database.
 */
@Entity
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String body;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private Blob image;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Post(User user, String body) {
        this.user = user;
        this.body = body;
        this.createdAt = LocalDateTime.now(); // todo: timezone?
    }
}
