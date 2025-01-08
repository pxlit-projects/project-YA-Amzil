import { RoleService } from './../../../shared/services/role.service';
import { Review } from './../../../shared/models/review.model';
import { Component, inject, OnInit } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { ReviewService } from '../../../shared/services/review.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';

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
  roleService: RoleService = inject(RoleService);
  router: Router = inject(Router);
  post!: Post;
  fb: FormBuilder = inject(FormBuilder);
  reviewer = this.roleService.getCurrentUser();

  reviewForm: FormGroup = this.fb.group({
    action: ['', [Validators.required]],
    // reviewer: ['', [Validators.required]],
    reviewer: [this.reviewer],
    comment: ['', [Validators.required]],
    reviewedAt: [new Date().toISOString()],
  });

  ngOnInit(): void {
    const postId = this.route.snapshot.params['id'];
    this.postService.getPostById(postId).subscribe((data: Post) => {
      this.post = data;
    });

    this.reviewForm.get('action')?.valueChanges.subscribe((action) => {
      if (action === 'reject') {
        // this.reviewForm.get('reviewer')?.setValidators([Validators.required]);
        this.reviewForm.get('comment')?.setValidators([Validators.required]);
      } else {
        // this.reviewForm.get('reviewer')?.clearValidators();
        this.reviewForm.get('comment')?.clearValidators();
      }
      // this.reviewForm.get('reviewer')?.updateValueAndValidity();
      this.reviewForm.get('comment')?.updateValueAndValidity();
    });
  }

  onApprovePost(): void {
    const postId = this.route.snapshot.params['id'];
    const review: Review = this.reviewForm.value as Review;
    review.postId = postId;
    this.reviewService.approveReview(review).subscribe(() => {
      this.router.navigate(['/review']);
    });
  }

  onRejectPost(): void {
    if (this.reviewForm.valid) {
      const postId = this.route.snapshot.params['id'];
      const review: Review = this.reviewForm.value as Review;
      review.postId = postId;
      this.reviewService.rejectReview(review).subscribe(() => {
        this.router.navigate(['/review']);
      });
    }
  }

  onSubmit(): void {
    const action = this.reviewForm.get('action')?.value;
    if (action === 'approve') {
      this.onApprovePost();
    } else if (action === 'reject') {
      this.onRejectPost();
    }
  }

  onCancel(): void {
    this.router.navigate(['/dashboard']);
  }
}
