package be.pxl.services.services;

// import be.pxl.services.client.NotificationClient;
import be.pxl.services.client.PostClient;
// import be.pxl.services.domain.NotificationRequest;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.ReviewStatus;
import be.pxl.services.domain.dto.PostRequest;
import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.exceptions.ReviewNotFoundException;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    // @Autowired
    private final PostClient postClient;
    // private final NotificationClient notificationClient;

    @Override
    public void createReview(ReviewRequest reviewRequest) {
        // Create a new Review object using the data from ReviewRequest
        Review review = Review.builder()
                .postId(reviewRequest.getPostId()) // Refers to the article that is being reviewed
                .reviewerId(reviewRequest.getReviewerId())
                .status(ReviewStatus.PENDING) // Status of the review (PENDING, APPROVED, REJECTED)
                .reviewer(reviewRequest.getReviewer()) // Name of the reviewer
                .comment(reviewRequest.getComment()) // Comment by the reviewer, if any
                .reviewedAt(LocalDateTime.now()) // Timestamp of review
                .build();

        // Save the review in the repository
        reviewRepository.save(review);
    }

    @Override
    public ReviewResponse approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with id [" + reviewId + "]"));

        review.setStatus(ReviewStatus.APPROVED);
        reviewRepository.save(review);

        // Update the post status based on approval
        PostRequest postRequest = new PostRequest();
        postRequest.setStatus(PostStatus.PUBLISHED);
        postClient.updatePost(review.getPostId(), postRequest);

        // Send notification for approval
//        NotificationRequest notificationRequest = new NotificationRequest();
//        notificationRequest.setMessage("Post approved");
//        notificationRequest.setSender(review.getReviewer());
//        notificationClient.sendNotification(notificationRequest);

        return mapToResponse(review);
    }

    @Override
    public ReviewResponse rejectReview(Long reviewId, ReviewRequest reviewRequest) {
        if (reviewRequest.getComment() == null || reviewRequest.getComment().isEmpty()) {
            throw new IllegalArgumentException("Comment is required for rejecting a post");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id [" + reviewId + "]"));

        review.setStatus(ReviewStatus.REJECTED);
        review.setComment(reviewRequest.getComment());
        review.setReviewedAt(reviewRequest.getReviewedAt());
        reviewRepository.save(review);

        // Update the post status based on rejection
        PostRequest postRequest = new PostRequest();
        postRequest.setStatus(PostStatus.REJECTED);
        postClient.updatePost(review.getPostId(), postRequest);

        // Send notification for rejection
//        NotificationRequest notificationRequest = new NotificationRequest();
//        notificationRequest.setMessage("Post rejected");
//        notificationRequest.setSender(review.getReviewer());
//        notificationClient.sendNotification(notificationRequest);

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
                .reviewerId(review.getReviewerId())
                .reviewer(review.getReviewer())
                .status(review.getStatus())
                .comment(review.getComment())
                .reviewedAt(review.getReviewedAt())
                .build();
    }
}
