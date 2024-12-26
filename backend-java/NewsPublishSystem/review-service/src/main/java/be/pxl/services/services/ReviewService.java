package be.pxl.services.services;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.client.PostClient;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.ReviewStatus;
import be.pxl.services.domain.dto.*;
import be.pxl.services.messaging.ReviewMessageProducer;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final PostClient postClient;
    private final ReviewMessageProducer reviewMessageProducer;
    private final NotificationClient notificationClient;

    @Override
    public ReviewResponse approveReview(Long postId) {

        PostRequest post = postClient.getPost(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with id [" + postId + "]");
        }

        // post.setStatus(PostStatus.PUBLISHED);
        // postClient.updatePost(postId, post);

        Review review = Review.builder()
                .postId(postId)
                .reviewer("Editor")
                .comment("Approved")
                .status(ReviewStatus.APPROVED)
                .reviewedAt(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

//        ReviewMessage reviewMessage = new ReviewMessage(postId, PostStatus.PUBLISHED);
//        reviewMessageProducer.sendMessage(reviewMessage);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .sender("Review Service")
                .message("Post with id [" + postId + "] has been approved")
                .build();

        notificationClient.sendNotification(notificationRequest);

        return mapToResponse(review);
    }

    @Override
    public ReviewResponse rejectReview(Long postId, ReviewRequest reviewRequest) {

        PostRequest post = postClient.getPost(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with id [" + postId + "]");
        }

        // post.setStatus(PostStatus.REJECTED);
        // postClient.updatePost(postId, post);

        Review review = Review.builder()
                .postId(postId)
                .status(ReviewStatus.REJECTED)
                .reviewer(reviewRequest.getReviewer())
                .comment(reviewRequest.getComment())
                .reviewedAt(reviewRequest.getReviewedAt())
                .build();

        reviewRepository.save(review);

//        ReviewMessage reviewMessage = new ReviewMessage(postId, PostStatus.REJECTED);
//        reviewMessageProducer.sendMessage(reviewMessage);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .sender("Review Service")
                .message("Post with id [" + postId + "] has been rejected")
                .build();

        notificationClient.sendNotification(notificationRequest);

        return mapToResponse(review);
    }

    @Override
    public List<ReviewResponse> getRejectedReviews() {
        return reviewRepository.findByStatus(ReviewStatus.REJECTED).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> getApprovedReviews() {
        return reviewRepository.findByStatus(ReviewStatus.APPROVED).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ReviewResponse publishPost(Long postId) {
        Review review = reviewRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found for post id [" + postId + "]"));

        review.setStatus(ReviewStatus.PUBLISHED);
        reviewRepository.save(review);

        ReviewMessage reviewMessage = new ReviewMessage(postId, PostStatus.PUBLISHED);
        reviewMessageProducer.sendMessage(reviewMessage);

        return mapToResponse(review);
    }

    @Override
    public ReviewResponse revisePost(Long postId) {
        Review review = reviewRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found for post id [" + postId + "]"));

        review.setStatus(ReviewStatus.PENDING);
        reviewRepository.save(review);

        ReviewMessage reviewMessage = new ReviewMessage(postId, PostStatus.PENDING);
        reviewMessageProducer.sendMessage(reviewMessage);

        return mapToResponse(review);
    }


    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReviewResponse mapToResponse(Review review) {
        // Convert the Review entity to a ReviewResponse DTO
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
