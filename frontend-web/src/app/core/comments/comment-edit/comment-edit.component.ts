import { Component, inject, OnInit } from '@angular/core';
import { CommentService } from '../../../shared/services/comment.service';
import { ActivatedRoute, Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Comment } from '../../../shared/models/comment.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-comment-edit',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './comment-edit.component.html',
  styleUrl: './comment-edit.component.css',
})
export class CommentEditComponent implements OnInit {
  commentService: CommentService = inject(CommentService);
  comment!: Comment;
  router: Router = inject(Router);
  route: ActivatedRoute = inject(ActivatedRoute);
  fb: FormBuilder = inject(FormBuilder);
  updateForm!: FormGroup;

  ngOnInit(): void {
    this.comment = history.state['comment'];
    this.updateForm = this.fb.group({
      content: [this.comment.content, [Validators.required]],
      updateAt: [new Date().toISOString()],
    });
  }

  onSubmit(): void {
    if (this.updateForm.valid) {
      const updatedComment: Comment = {
        ...this.comment,
        ...this.updateForm.value,
      };
      this.commentService.updateComment(updatedComment).subscribe(() => {
        this.router.navigate(['/home']);
      });
    }
  }

  OnCancel() {
    this.router.navigate(['/home']);
  }
}
