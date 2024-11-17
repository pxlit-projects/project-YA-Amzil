package be.pxl.services.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime editDate;
    private boolean isDeleted;
}
