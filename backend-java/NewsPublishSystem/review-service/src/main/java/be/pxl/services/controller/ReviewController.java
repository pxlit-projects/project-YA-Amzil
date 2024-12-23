package be.pxl.services.controller;

import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.services.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    // Endpoint to create a new review
    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody ReviewRequest reviewRequest) {
        reviewService.createReview(reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/approve/{reviewId}")
    public ResponseEntity<ReviewResponse> approveReview(@PathVariable Long reviewId) {
        ReviewResponse approvedReview = reviewService.approveReview(reviewId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(approvedReview);
    }

    @PutMapping("/reject/{reviewId}")
    public ResponseEntity<ReviewResponse> rejectReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse rejectedReview = reviewService.rejectReview(reviewId, reviewRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rejectedReview);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

//    // Endpoint to update the status of a review
//    @PutMapping("/{reviewId}")
//    public ResponseEntity<ReviewResponse> updateReviewStatus(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest) {
//        ReviewResponse updateReview = reviewService.updateReviewStatus(reviewId, reviewRequest);
//        return  ResponseEntity.status(HttpStatus.ACCEPTED).body(updateReview);
//    }
//
//    // Endpoint to get all reviews for a specific post
//    @GetMapping("/post/{postId}")
//    public ResponseEntity<List<ReviewResponse>> getReviewsForPost(@PathVariable Long postId) {
//        List<ReviewResponse> reviewResponse = reviewService.getReviewsForPost(postId);
//        return ResponseEntity.status(HttpStatus.OK).body(reviewResponse);
//    }
//
//    // Endpoint to get all reviews made by a specific reviewer
//    @GetMapping("/reviewer/{reviewerId}")
//    public ResponseEntity<List<ReviewResponse>> getReviewsByReviewer(@PathVariable Long reviewerId) {
//        List<ReviewResponse> reviewResponse = reviewService.getReviewsByReviewer(reviewerId);
//        return ResponseEntity.status(HttpStatus.OK).body(reviewResponse);
//    }
//
//    // Endpoint to get all reviews with a specific status (e.g., PENDING, APPROVED, REJECTED)
//    @GetMapping("/status/{status}")
//    public ResponseEntity<List<ReviewResponse>> getReviewsByStatus(@PathVariable String status) {
//        List<ReviewResponse> reviewResponses = reviewService.getReviewsByStatus(status);
//        return ResponseEntity.status(HttpStatus.OK).body(reviewResponses);
//    }
}
