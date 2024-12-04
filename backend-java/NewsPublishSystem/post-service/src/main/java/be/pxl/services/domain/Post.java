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

    // @NotNull(message = "Title cannot be null")
    // @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    // @NotNull(message = "Content cannot be null")
    // @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;

    // @NotNull(message = "Category cannot be null")
    // @Size(min = 3, message = "Category must be at least 3 characters long")
    private String category;

    //@NotNull(message = "Author cannot be null")
    private String author;

    // @Column(nullable = false)
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    private PostStatus status;

    // private List<Review> reviews; @Transient
    // private List<Comment> comments; @Transient
}
