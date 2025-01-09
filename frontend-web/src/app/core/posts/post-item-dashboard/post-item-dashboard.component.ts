import { Component, inject, Input } from '@angular/core';
import { Post } from '../../../shared/models/post.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-item-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-item-dashboard.component.html',
  styleUrl: './post-item-dashboard.component.css'
})
export class PostItemDashboardComponent {
 @Input() post!: Post;
  router: Router = inject(Router);

  onEdit(post: Post) {
    this.router.navigate(['/post-edit', post.id], { state: { post } });
  }

  onReview(post: Post) {
    this.router.navigate(['/review-post', post.id]);
  }
}
