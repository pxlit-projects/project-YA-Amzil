package be.pxl.services.services;

import be.pxl.services.domain.Review;
import be.pxl.services.domain.ReviewStatus;
import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.exceptions.ReviewNotFoundException;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    // US7: Als hoofdredacteur wil ik ingediende artikelen kunnen bekijken en goedkeuren of afwijzen, zodat alleen goedgekeurde content wordt gepubliceerd.
    // This method allows the main editor (hoofdredacteur) to view reviews (i.e., reviews for articles) and approve or reject them.
    @Override
    public void createReview(ReviewRequest reviewRequest) {
        // Create a new Review object using the data from ReviewRequest
        Review review = Review.builder()
                .postId(reviewRequest.getPostId()) // Refers to the article that is being reviewed
                .reviewerId(reviewRequest.getReviewerId())
                .status(ReviewStatus.valueOf(reviewRequest.getStatus().toUpperCase())) // Status (approved/rejected)
                .comment(reviewRequest.getComment()) // Comment by the reviewer, if any
                .reviewedAt(LocalDateTime.now()) // Timestamp of review
                .build();

        // Save the review in the repository
        reviewRepository.save(review);
    }

    // US7: Allows the main editor to update the status (approve/reject) of the review and provide feedback
    // US9: Als redacteur wil ik opmerkingen kunnen toevoegen bij afwijzing van een artikel, zodat de redacteur weet welke wijzigingen er nodig zijn.
    // This method is used by the main editor to update the review's status (approve/reject) and add comments when rejected.
    @Override
    public ReviewResponse updateReviewStatus(Long reviewId, ReviewRequest reviewRequest) {
        // Fetch the review by its ID
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id [" + reviewId + "]"));

        // Update the review's status and comment (if rejected)
        review.setStatus(ReviewStatus.valueOf(reviewRequest.getStatus().toUpperCase()));

        // If the review is rejected, the editor can provide a comment
        if (ReviewStatus.REJECTED.equals(review.getStatus()) && reviewRequest.getComment() != null) {
            review.setComment(reviewRequest.getComment());
        }

        // Update the timestamp when the review was processed
        review.setReviewedAt(LocalDateTime.now());

        // Save the updated review
        reviewRepository.save(review);

        // Return the updated review as a response DTO
        return mapToResponse(review);
    }

    // US7: View all reviews related to a specific post (article)
    @Override
    public List<ReviewResponse> getReviewsForPost(Long postId) {
        // Fetch all reviews for a specific post
        List<Review> reviews = reviewRepository.findByPostId(postId);
        return reviews.stream()
                .map(this::mapToResponse) // Map each review to a ReviewResponse DTO
                .toList();
    }

    // US8: Redacteur will be notified of the approval/rejection; This method retrieves reviews by the reviewer.
    @Override
    public List<ReviewResponse> getReviewsByReviewer(Long reviewerId) {
        // Fetch all reviews made by a specific reviewer (editor)
        List<Review> reviews = reviewRepository.findByReviewerId(reviewerId);
        return reviews.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // US7: View all reviews with a specific status (e.g., PENDING, APPROVED, REJECTED)
    @Override
    public List<ReviewResponse> getReviewsByStatus(String status) {
        // Fetch all reviews with a specific status (PENDING, APPROVED, REJECTED)
        List<Review> reviews = reviewRepository.findByStatus(ReviewStatus.valueOf(status.toUpperCase()));
        return reviews.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Helper method to map a Review entity to a ReviewResponse DTO
    private ReviewResponse mapToResponse(Review review) {
        // Convert the Review entity to a ReviewResponse DTO
        return ReviewResponse.builder()
                .id(review.getId())
                .postId(review.getPostId())
                .reviewerId(review.getReviewerId())
                .status(String.valueOf(review.getStatus())) // Converts the status to a string
                .comment(review.getComment())
                .reviewedAt(review.getReviewedAt())
                .build();
    }
}
