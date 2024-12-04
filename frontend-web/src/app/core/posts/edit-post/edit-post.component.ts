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
  // router: Router = inject(Router);
  fb: FormBuilder = inject(FormBuilder);
  postForm: FormGroup = this.fb.group({
    // id: 0,
    title: ['', [Validators.required]],
    content: ['', [Validators.required]],
    category: ['', [Validators.required]],
    author: ['', [Validators.required]],
    // updateAt: new Date(),
  });

  ngOnInit(): void {
    this.post = history.state['post'];
    this.postForm.patchValue({
      title: this.post.title,
      content: this.post.content,
      category: this.post.category,
      author: this.post.author,
    });
  }

  onSubmit(): void {
    if (this.postForm.valid) {
      const post: Post = this.postForm.value as Post;
      this.editPost(post);
    }
  }

  editPost(post: Post) {
    this.postService.updatePost(post).subscribe(() => {
      // this.router.navigate(['/posts']);
    });
  }

  // cancel(): void {
  //   this.router.navigate(['/posts']);
  // }
}
