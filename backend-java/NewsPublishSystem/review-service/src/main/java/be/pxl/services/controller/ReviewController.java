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

    @PostMapping("/approve/{postId}")
    public ResponseEntity<ReviewResponse> approveReview(@PathVariable Long postId) {
        ReviewResponse approvedReview = reviewService.approveReview(postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(approvedReview);
    }

    @PostMapping("/reject/{postId}")
    public ResponseEntity<ReviewResponse> rejectReview(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse rejectedReview = reviewService.rejectReview(postId, reviewRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rejectedReview);
    }

    @PutMapping("/publish/{postId}")
    public ResponseEntity<ReviewResponse> publishPost(@PathVariable Long postId) {
       ReviewResponse publishedPost = reviewService.publishPost(postId);
       return ResponseEntity.status(HttpStatus.ACCEPTED).body(publishedPost);
    }

    @PutMapping("/revise/{postId}")
    public ResponseEntity<ReviewResponse> revisePost(@PathVariable Long postId) {
        ReviewResponse revisedPost = reviewService.revisePost(postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(revisedPost);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ReviewResponse>> getApprovedReviews() {
        List<ReviewResponse> reviews = reviewService.getApprovedReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<ReviewResponse>> getRejectedReviews() {
        List<ReviewResponse> reviews = reviewService.getRejectedReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}


