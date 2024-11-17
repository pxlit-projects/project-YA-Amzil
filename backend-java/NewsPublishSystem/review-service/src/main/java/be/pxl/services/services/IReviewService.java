package be.pxl.services.services;

import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {
    // Method to create a new review
    void createReview(ReviewRequest reviewRequest);

    // Method to update the status of a review (approve or reject)
    ReviewResponse updateReviewStatus(Long reviewId, ReviewRequest reviewRequest);

    // Method to get all reviews for a specific post
    List<ReviewResponse> getReviewsForPost(Long postId);

    // Method to get all reviews done by a specific reviewer
    List<ReviewResponse> getReviewsByReviewer(Long reviewerId);

    // Method to get all reviews with a specific status (e.g., pending, approved, rejected)
    List<ReviewResponse> getReviewsByStatus(String status);
}
