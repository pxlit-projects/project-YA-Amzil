package be.pxl.services.domain.dto;

import be.pxl.services.domain.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long postId;
    private Long reviewerId;
    private String reviewer;
    private ReviewStatus status;
    private String comment;
    private LocalDateTime reviewedAt;
}
