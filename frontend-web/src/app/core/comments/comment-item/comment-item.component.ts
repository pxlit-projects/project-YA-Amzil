import { CommentService } from './../../../shared/services/comment.service';
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
  commentService: CommentService = inject(CommentService);
  router: Router = inject(Router);
  roleService: RoleService = inject(RoleService);
  // route: ActivatedRoute = inject(ActivatedRoute);
  role = this.roleService.getRole();
  currentUser = this.roleService.getCurrentUser();

  onEdit(comment: Comment) {
      this.router.navigate(['/comment-edit', comment.id], { state: { comment } });
  }

  onDelete(comment: Comment) {
    if (comment.id !== undefined) {
      this.commentService.deleteComment(comment.id).subscribe(() => {
        window.location.reload();
      });
    } else {
      console.error('Comment ID is undefined');
    }
  }
}
