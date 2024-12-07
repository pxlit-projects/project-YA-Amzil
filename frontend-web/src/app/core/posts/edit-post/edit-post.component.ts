import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../shared/services/post.service';
import { Post } from '../../../shared/models/post.model';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-post',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './edit-post.component.html',
  styleUrl: './edit-post.component.css',
})
export class EditPostComponent implements OnInit {
  postService: PostService = inject(PostService);
  post!: Post;
  router: Router = inject(Router);
  route: ActivatedRoute = inject(ActivatedRoute);
  fb: FormBuilder = inject(FormBuilder);
  updateForm!: FormGroup;

  ngOnInit(): void {
    this.post = history.state['post'];
    this.updateForm = this.fb.group({
      title: [this.post.title, [Validators.required]],
      content: [this.post.content, [Validators.required]],
      author: [this.post.author, [Validators.required]],
      status: [this.post.status],
      updateAt: [new Date().toISOString()],
    });
  }

  onSubmit(): void {
    if (this.updateForm.valid) {
      const updatedPost: Post = { ...this.post, ...this.updateForm.value };
      this.postService.updatePost(updatedPost).subscribe(() => {
      this.router.navigate(['/home']);
      });
    }
  }

  // OnCancel() {
  //   this.router.navigate(['/posts']);
  // }
}

