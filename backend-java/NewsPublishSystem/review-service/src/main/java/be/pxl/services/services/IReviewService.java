package be.pxl.services.services;

import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {

    // Method to approve a review
    ReviewResponse approveReview(Long postId, ReviewRequest reviewRequest);

    // Method to reject a review
    ReviewResponse rejectReview(Long postId, ReviewRequest reviewRequest);

    //Method to get rejected reviews
    List<ReviewResponse> getRejectedReviews();

    // Method to get approved reviews
    List<ReviewResponse> getApprovedReviews();

    // Method to publish a post
    ReviewResponse publishPost(Long postId);

    // Method to revise a post
    ReviewResponse revisePost(Long postId);

    // Method to get all reviews
    List<ReviewResponse> getAllReviews();

    // Method to delete a review
    boolean deleteReview(Long reviewId);
}
