import { ReviewService } from '../../../shared/services/review.service';
import { Review } from './../../../shared/models/review.model';
import { Component, inject, OnInit } from '@angular/core';
import { ReviewItemComponent } from "../review-item/review-item.component";

@Component({
  selector: 'app-review-list',
  standalone: true,
  imports: [ReviewItemComponent],
  templateUrl: './review-list.component.html',
  styleUrl: './review-list.component.css',
})
export class ReviewListComponent implements OnInit {
  review!: Review;
  approvedData: Review[] = [];
  rejectedData: Review[] = [];

  reviewService: ReviewService = inject(ReviewService);

  ngOnInit(): void {
    this.getApprovedPosts();
    this.getRejectedPosts();
  }

  getApprovedPosts() {
    this.reviewService.getAllApprovedPosts().subscribe({
      next: (posts) => {
        this.approvedData = posts;
      },
      error: (err) => {
        console.error('Error fetching approved posts:', err);
        this.approvedData = [];
      },
    });
  }

  getRejectedPosts() {
    this.reviewService.getAllRejectedPosts().subscribe({
      next: (posts) => {
        this.rejectedData = posts;
      },
      error: (err) => {
        console.error('Error fetching rejected posts:', err);
        this.rejectedData = [];
      },
    });
  }
}
