import { Component, inject } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { CommentService } from '../../../shared/services/comment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Comment } from '../../../shared/models/comment.model';

@Component({
  selector: 'app-comment-post',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './comment-post.component.html',
  styleUrl: './comment-post.component.css',
})
export class CommentPostComponent {
  postService: PostService = inject(PostService);
  commentService: CommentService = inject(CommentService);
  router: Router = inject(Router);
  route: ActivatedRoute = inject(ActivatedRoute);
  fb: FormBuilder = inject(FormBuilder);

  commentForm: FormGroup = this.fb.group({
    postId: [this.route.snapshot.params['id']],
    author: ['', [Validators.required]],
    content: ['', [Validators.required]],
    createAt: [new Date().toISOString()],
    updateAt: [new Date().toISOString()],
  });

  onSubmit(): void {
    if (this.commentForm.valid) {
      const comment: Comment = this.commentForm.value as Comment;
      this.commentService.createComment(comment).subscribe(() => {
        this.commentForm.reset();
        this.router.navigate(['/home']);
      });
    }
  }

  OnCancel() {
    this.router.navigate(['/home']);
  }
}
