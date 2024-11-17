package be.pxl.services.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title; // Title of the post

    @NotNull(message = "Content cannot be null")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content; // Content of the post

    @NotNull(message = "Author cannot be null")
    private String author; // Author of the post

    @Column(nullable = false)
    private LocalDateTime creationDate; // Timestamp of when the post was created

    private LocalDateTime lastUpdateDate; // Timestamp of the last update to the post

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status; // Status of the post (e.g., DRAFT, PUBLISHED)

    private String category; // Category of the post

    @PrePersist
    @PreUpdate
    protected void onPersistOrUpdate() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now(); // Set creationDate only if it's null (during persist)
        }
        this.lastUpdateDate = LocalDateTime.now(); // Set or update lastUpdateDate every time the post is persisted or updated
    }
}
