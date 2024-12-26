import { Component, inject } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { Post } from '../../../shared/models/post.model';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css',
})
export class CreatePostComponent {
  postService: PostService = inject(PostService);
  router: Router = inject(Router);
  route: ActivatedRoute = inject(ActivatedRoute);
  fb: FormBuilder = inject(FormBuilder);

  postForm: FormGroup = this.fb.group({
    title: ['', [Validators.required]],
    content: ['', [Validators.required]],
    author: ['', [Validators.required]],
    createAt: [new Date().toISOString()],
    updateAt: [new Date().toISOString()],
    status: ['', [Validators.required]],
  });

  onSubmit(): void {
    if (this.postForm.valid) {
      const post: Post = this.postForm.value as Post;
      this.postService.createPost(post).subscribe(() => {
        this.postForm.reset();
        this.router.navigate(['/dashboard']);
      });
    }
  }

  OnCancel() {
    this.router.navigate(['/home']);
  }
}
