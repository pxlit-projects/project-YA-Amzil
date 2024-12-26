package be.pxl.services.domain.dto;

import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMessage {
    private Long postId;
    private PostStatus status;
}
