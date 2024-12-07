import { Component, inject, Input } from '@angular/core';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-post-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-item.component.html',
  styleUrl: './post-item.component.css',
})
export class PostItemComponent {
  @Input() post!: Post;
  router: Router = inject(Router);
  route: ActivatedRoute = inject(ActivatedRoute);

  onEdit(post: Post) {
    this.router.navigate(['/edit', post.id], { state: { post } });
  }
}
