package be.pxl.services.messaging;

import be.pxl.services.domain.dto.ReviewMessage;
import be.pxl.services.services.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageConsumer {
    private final IPostService postService;

    @RabbitListener(queues = "postReviewQueue")
    public void consumerMessage(ReviewMessage reviewMessage) {
        postService.updatePostStatus(reviewMessage.getPostId(), reviewMessage.getStatus());
    }
}
