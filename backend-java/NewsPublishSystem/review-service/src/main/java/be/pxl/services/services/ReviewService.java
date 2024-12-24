package be.pxl.services.services;

// import be.pxl.services.client.NotificationClient;
import be.pxl.services.client.PostClient;
// import be.pxl.services.domain.NotificationRequest;
import be.pxl.services.domain.Post;
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
    private final PostClient postClient;
    // private final NotificationClient notificationClient;

    @Override
    public void createReview(ReviewRequest reviewRequest) {
        // Create a new Review object using the data from ReviewRequest
        Review review = Review.builder()
                .postId(reviewRequest.getPostId()) // Refers to the article that is being reviewed
                .status(ReviewStatus.PENDING) // Status of the review (PENDING, APPROVED, REJECTED)
                .reviewer(reviewRequest.getReviewer()) // Name of the reviewer
                .comment(reviewRequest.getComment()) // Comment by the reviewer, if any
                .reviewedAt(LocalDateTime.now()) // Timestamp of review
                .build();

        // Save the review in the repository
        reviewRepository.save(review);
    }

    @Override
    public ReviewResponse approveReview(Long postId) {

        PostRequest post = postClient.getPost(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with id [" + postId + "]");
        }

        post.setStatus(PostStatus.PUBLISHED);
        postClient.updatePost(postId, post);

        Review review = Review.builder()
                .postId(postId)
                .status(ReviewStatus.APPROVED)
                .build();

        reviewRepository.save(review);

        return ReviewResponse.builder()
                .postId(postId)
                .status(ReviewStatus.APPROVED)
                .build();
    }

    @Override
    public ReviewResponse rejectReview(Long postId, ReviewRequest reviewRequest) {

        PostRequest post = postClient.getPost(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with id [" + postId + "]");
        }

        post.setStatus(PostStatus.REJECTED);
        postClient.updatePost(postId, post);

        Review review = Review.builder()
                .postId(postId)
                .status(ReviewStatus.REJECTED)
                .reviewer(reviewRequest.getReviewer())
                .comment(reviewRequest.getComment())
                .reviewedAt(reviewRequest.getReviewedAt())
                .build();

        reviewRepository.save(review);

        return ReviewResponse.builder()
                .postId(postId)
                .reviewer(reviewRequest.getReviewer())
                .status(ReviewStatus.REJECTED)
                .comment(reviewRequest.getComment())
                .reviewedAt(reviewRequest.getReviewedAt())
                .build();
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
