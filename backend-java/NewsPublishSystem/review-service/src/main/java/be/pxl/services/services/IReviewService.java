package be.pxl.services.services;

import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {

    // Method to approve a review
    ReviewResponse approveReview(Long postId);

    // Method to reject a review
    ReviewResponse rejectReview(Long postId, ReviewRequest reviewRequest);

    //Method to get rejected reviews
    List<ReviewResponse> getRejectedReviews();

    // Method to get all reviews
    List<ReviewResponse> getAllReviews();
}
