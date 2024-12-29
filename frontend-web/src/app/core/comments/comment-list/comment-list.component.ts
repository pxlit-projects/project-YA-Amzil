import { Component, inject, OnInit } from '@angular/core';
import { CommentItemComponent } from "../comment-item/comment-item.component";
import { CommentService } from '../../../shared/services/comment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Comment } from '../../../shared/models/comment.model';

@Component({
  selector: 'app-comment-list',
  standalone: true,
  imports: [CommentItemComponent],
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.css',
})
export class CommentListComponent implements OnInit {
  comment!: Comment;
  commentData: Comment[] = [];
  commentService: CommentService = inject(CommentService);
  router: Router = inject(Router);
  route: ActivatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    const postId = this.route.snapshot.params['id'];
    this.getCommentsForPost(postId);
  }

  getCommentsForPost(postId: number): void {
    this.commentService.getCommentsForPost(postId).subscribe((comments) => {
      this.commentData = comments;
  });
  }
}
