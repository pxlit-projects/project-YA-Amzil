import { Review } from './../../../shared/models/review.model';
import { Component, inject, OnInit } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { ReviewService } from '../../../shared/services/review.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-review-post',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './review-post.component.html',
  styleUrl: './review-post.component.css',
})
export class ReviewPostComponent implements OnInit {
  postService: PostService = inject(PostService);
  reviewService: ReviewService = inject(ReviewService);
  route: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  post!: Post;
  fb: FormBuilder = inject(FormBuilder);

  reviewForm: FormGroup = this.fb.group({
    reviewer: ['', [Validators.required]],
    comment: ['', [Validators.required]],
    reviewedAt: [new Date().toISOString()],
  });

  ngOnInit(): void {
    const postId = this.route.snapshot.params['id'];
    this.postService.getPostById(postId).subscribe((data: Post) => {
      this.post = data;
    });
  }

  OnApprovePost(): void {
    const postId = this.route.snapshot.params['id'];
    this.reviewService.approveReview(postId).subscribe(() => {
      this.router.navigate(['/home']);
    });
  }

  OnRejectPost(): void {
    if (this.reviewForm.valid) {
      const postId = this.route.snapshot.params['id'];
      const review: Review = this.reviewForm.value as Review;
      review.postId = postId;
      this.reviewService.rejectReview(review).subscribe(() => {
        this.router.navigate(['/home']);
      });
    }
  }

  OnCancel(): void {
    this.router.navigate(['/dashboard']);
  }
}