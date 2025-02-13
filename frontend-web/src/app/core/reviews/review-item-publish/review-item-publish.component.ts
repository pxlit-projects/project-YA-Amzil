import { Component, inject, Input } from '@angular/core';
import { Review } from '../../../shared/models/review.model';
import { ReviewService } from '../../../shared/services/review.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-review-item-publish',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './review-item-publish.component.html',
  styleUrl: './review-item-publish.component.css'
})
export class ReviewItemPublishComponent {
  @Input() review!: Review;
    reviewService: ReviewService = inject(ReviewService);
    router: Router = inject(Router);

    onPublish(postId: number): void {
      this.reviewService.publishPost(postId).subscribe({
        next: () => {
          this.router.navigate(['/home']).then(() => {
            window.location.reload();
          });
        },
        error: (err) => {
          console.error('Error publishing post:', err);
        },
      });
    }
}
