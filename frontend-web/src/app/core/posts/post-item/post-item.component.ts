import { Component, inject, Input } from '@angular/core';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
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
  router: Router = inject(Router);
  roleService: RoleService = inject(RoleService);
  // route: ActivatedRoute = inject(ActivatedRoute);
  role = this.roleService.getRole();

  onComment(post: Post) {
    this.router.navigate(['/comment-post', post.id]);
  }

  onReadmore(post: Post) {
    this.router.navigate(['/read-post', post.id]);
  }
}
