package be.pxl.services.controller;

import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.domain.dto.ReviewResponse;
import be.pxl.services.services.IReviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @PutMapping("/approve/{postId}")
    public ResponseEntity<ReviewResponse> approveReview(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse approvedReview = reviewService.approveReview(postId, reviewRequest);
        log.info("Approving review for post with id: {}", postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(approvedReview);
    }

    @PutMapping("/reject/{postId}")
    public ResponseEntity<ReviewResponse> rejectReview(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse rejectedReview = reviewService.rejectReview(postId, reviewRequest);
        log.info("Rejecting review for post with id: {}", postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rejectedReview);
    }

    @PutMapping("/publish/{postId}")
    public ResponseEntity<ReviewResponse> publishPost(@PathVariable Long postId) {
       ReviewResponse publishedPost = reviewService.publishPost(postId);
       log.info("Publishing post with id: {}", postId);
       return ResponseEntity.status(HttpStatus.ACCEPTED).body(publishedPost);
    }

    @PutMapping("/revise/{postId}")
    public ResponseEntity<ReviewResponse> revisePost(@PathVariable Long postId) {
        ReviewResponse revisedPost = reviewService.revisePost(postId);
        log.info("Revising post with id: {}", postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(revisedPost);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        log.info("Getting all reviews");
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ReviewResponse>> getApprovedReviews() {
        List<ReviewResponse> reviews = reviewService.getApprovedReviews();
        log.info("Getting all approved reviews");
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<ReviewResponse>> getRejectedReviews() {
        List<ReviewResponse> reviews = reviewService.getRejectedReviews();
        log.info("Getting all rejected reviews");
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        boolean isDeleted = reviewService.deleteReview(reviewId);
        log.info("Deleting review with id: {}", reviewId);
        return isDeleted ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}


