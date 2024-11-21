import { Component } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { Post } from '../../../shared/models/post.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css',
})
export class CreatePostComponent {
  post: Post = {
    id: 0,
    title: '',
    content: '',
    author: '',
    category: '',
    status: 'draft',
    createdDate: new Date(),
  };

  constructor(private postService: PostService) {}

  addPost(): void {
    this.postService.addPost(this.post).subscribe();
  }
}
