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

    @PutMapping("/approve/{postId}")
    public ResponseEntity<ReviewResponse> approveReview(@PathVariable Long postId) {
        ReviewResponse approvedReview = reviewService.approveReview(postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(approvedReview);
    }

    @PutMapping("/reject/{postId}")
    public ResponseEntity<ReviewResponse> rejectReview(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse rejectedReview = reviewService.rejectReview(postId, reviewRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rejectedReview);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews();
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}
