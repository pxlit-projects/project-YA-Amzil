import { ActivatedRoute, Router } from '@angular/router';
import { Review } from '../../../shared/models/review.model';
import { Component, inject, Input } from '@angular/core';
import { RoleService } from '../../../shared/services/role.service';
import { CommonModule } from '@angular/common';
import { ReviewService } from '../../../shared/services/review.service';

@Component({
  selector: 'app-review-item-revise',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './review-item-revise.component.html',
  styleUrl: './review-item-revise.component.css',
})
export class ReviewItemReviseComponent {
  @Input() review!: Review;
  reviewService: ReviewService = inject(ReviewService);
  router: Router = inject(Router);
  roleService: RoleService = inject(RoleService);
  // route: ActivatedRoute = inject(ActivatedRoute);
  role = this.roleService.getRole();

  onRevise(postId: number): void {
    this.reviewService.revisePost(postId).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']).then(() => {
          window.location.reload();
        });
      },
      error: (err) => {
        console.error('Error revising post:', err);
      },
    });
  }
}
