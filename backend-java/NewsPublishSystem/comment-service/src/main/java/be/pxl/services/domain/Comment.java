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
@Table(name = "comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    private LocalDateTime editDate;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @PrePersist
    @PreUpdate
    protected void onPersistOrUpdate() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now(); // Set creationDate only if it's null (during persist)
        }
        this.editDate = LocalDateTime.now(); // Set or update lastUpdateDate every time the post is persisted or updated
    }
}
