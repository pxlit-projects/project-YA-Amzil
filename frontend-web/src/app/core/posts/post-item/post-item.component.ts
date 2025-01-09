import { Component, inject, Input } from '@angular/core';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RoleService } from '../../../shared/services/role.service';

@Component({
  selector: 'app-post-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-item.component.html',
  styleUrl: './post-item.component.css',
})
export class PostItemComponent {
  @Input() post!: Post;
  roleService: RoleService = inject(RoleService);
  role = this.roleService.getRole();
  router: Router = inject(Router);

  onComment(post: Post) {
    this.router.navigate(['/comment-post', post.id]);
  }

  onEdit(post: Post) {
    this.router.navigate(['/post-edit', post.id], { state: { post } });
  }

  onReadmore(post: Post) {
    this.router.navigate(['/read-post', post.id]);
  }
}
