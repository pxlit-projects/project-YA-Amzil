import { PostService } from './../../../shared/services/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Review } from './../../../shared/models/review.model';
import { Component, inject, Input } from '@angular/core';
import { RoleService } from '../../../shared/services/role.service';
import { CommonModule } from '@angular/common';
import { ReviewService } from '../../../shared/services/review.service';

@Component({
  selector: 'app-review-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './review-item.component.html',
  styleUrl: './review-item.component.css',
})
export class ReviewItemComponent {
  @Input() review!: Review;
  reviewService: ReviewService = inject(ReviewService);
  router: Router = inject(Router);
  roleService: RoleService = inject(RoleService);
  route: ActivatedRoute = inject(ActivatedRoute);
  role = this.roleService.getRole();

  OnPublish(postId: number): void {
    this.reviewService.publishPost(postId).subscribe(() => {
      this.router.navigate(['/home']);
    });
  }

  OnRevise(postId: number): void {
    this.reviewService.revisePost(postId).subscribe(() => {
      this.router.navigate(['/dashboard']);
    });
  }
}


