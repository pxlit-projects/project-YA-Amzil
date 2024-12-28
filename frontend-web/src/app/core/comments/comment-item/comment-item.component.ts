import { Component, inject, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoleService } from '../../../shared/services/role.service';
import { CommonModule } from '@angular/common';
import { Comment } from '../../../shared/models/comment.model';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css'
})
export class CommentItemComponent {
  @Input() comment!: Comment;
  router: Router = inject(Router);
  roleService: RoleService = inject(RoleService);
  route: ActivatedRoute = inject(ActivatedRoute);
  role = this.roleService.getRole();

  // onEdit(post: Post) {
  //     this.router.navigate(['/edit', post.id], { state: { post } });
  //   }

  //   onDelete(){

  //   }

}
