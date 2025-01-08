package be.pxl.services.services;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.ReviewStatus;
import be.pxl.services.domain.dto.*;
import be.pxl.services.exceptions.ReviewNotFoundException;
import be.pxl.services.messaging.ReviewMessageProducer;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final NotificationClient notificationClient;
    private final ReviewMessageProducer reviewMessageProducer;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    // US6 - US7
    @Override
    public ReviewResponse approveReview(Long postId, ReviewRequest reviewRequest) {
        log.info("Approving review for post with id: {}", postId);

        Review review = reviewRepository.findByPostId(postId).orElse(new Review());
        if (review.getId() == null) {
            log.info("Review not found for post with id: {}, creating a new review", postId);
            review.setPostId(postId);
        }
        review.setReviewer(reviewRequest.getReviewer());
        review.setComment("This post is approved");
        review.setStatus(ReviewStatus.APPROVED);
        review.setReviewedAt(reviewRequest.getReviewedAt());

        reviewRepository.save(review);
        log.info("Review approved successfully for post with id: {}", postId);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to("team.aon03@gmail.com")
                .subject("Post with id [" + postId + "] has been approved")
                .text("You have received a notification for the approval of post with id [" + postId + "] " + "by editor" )
                .build();

        notificationClient.sendNotification(notificationRequest);
        log.info("Notification sent successfully for post with id: {}", postId);

        return mapToResponse(review);
    }

    // US6 - US7 - US8
    @Override
    public ReviewResponse rejectReview(Long postId, ReviewRequest reviewRequest) {
        log.info("Rejecting review for post with id: {}", postId);

        Review review = reviewRepository.findByPostId(postId).orElse(new Review());
        if (review.getId() == null) {
            log.info("Review not found for post with id: {}, creating a new review", postId);
            review.setPostId(postId);
        }

        review.setReviewer(reviewRequest.getReviewer());
        review.setComment(reviewRequest.getComment());
        review.setStatus(ReviewStatus.REJECTED);
        review.setReviewedAt(reviewRequest.getReviewedAt());

        reviewRepository.save(review);
        log.info("Review rejected successfully for post with id: {}", postId);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to("team.aon03@gmail.com")
                .subject("Post with id [" + postId + "] has been rejected")
                .text("You have received a notification for the rejection of post with id [" + postId + "] " + "by reviewer [" + reviewRequest.getReviewer() + "]")
                .build();

        notificationClient.sendNotification(notificationRequest);
        log.info("Notification sent successfully for post with id: {}", postId);

        return mapToResponse(review);
    }

    // US7
    @Override
    public ReviewResponse publishPost(Long postId) {
        log.info("Publishing post with id: {}", postId);

        Review review = reviewRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Review not found for post with id: {}", postId);
                    return new ReviewNotFoundException("Review not found for post id [" + postId + "]");
                });

        // review.setReviewer("Editor");
        review.setComment("This post is published");
        review.setStatus(ReviewStatus.PUBLISHED);
        reviewRepository.save(review);
        log.info("Post published successfully with id: {}", postId);

        ReviewMessage reviewMessage = new ReviewMessage(postId, PostStatus.PUBLISHED);
        reviewMessageProducer.sendMessage(reviewMessage);
        log.info("Review message sent successfully for post with id: {}", postId);

        return mapToResponse(review);
    }

    // US7
    @Override
    public ReviewResponse revisePost(Long postId) {
        log.info("Revising post with id: {}", postId);

        Review review = reviewRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Review not found for post with id: {}", postId);
                    return new ReviewNotFoundException("Review not found for post id [" + postId + "]");
                });

        // review.setReviewer("Editor");
        review.setComment("This post is revised");
        review.setStatus(ReviewStatus.PENDING);
        reviewRepository.save(review);
        log.info("Post revised successfully with id: {}", postId);

        ReviewMessage reviewMessage = new ReviewMessage(postId, PostStatus.PENDING);
        reviewMessageProducer.sendMessage(reviewMessage);
        log.info("Review message sent successfully for post with id: {}", postId);

        return mapToResponse(review);
    }

    @Override
    public List<ReviewResponse> getRejectedReviews() {
        log.info("Getting all rejected reviews");
        return reviewRepository.findByStatus(ReviewStatus.REJECTED).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> getApprovedReviews() {
        log.info("Getting all approved reviews");
        return reviewRepository.findByStatus(ReviewStatus.APPROVED).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        log.info("Getting all reviews");
        return reviewRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        log.info("Deleting review with id: {}", reviewId);
        return reviewRepository.findById(reviewId)
                .map(review -> {
                    reviewRepository.delete(review);
                    return true;
                })
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id [" + reviewId + "]"));
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .postId(review.getPostId())
                .reviewer(review.getReviewer())
                .status(review.getStatus())
                .comment(review.getComment())
                .reviewedAt(review.getReviewedAt())
                .build();
    }
}
