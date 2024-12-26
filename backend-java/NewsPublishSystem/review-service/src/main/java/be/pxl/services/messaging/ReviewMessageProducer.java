package be.pxl.services.messaging;

import be.pxl.services.domain.dto.ReviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(ReviewMessage reviewMessage) {
        rabbitTemplate.convertAndSend("postReviewQueue", reviewMessage);
    }
}
