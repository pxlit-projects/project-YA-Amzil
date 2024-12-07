import { Component, inject, OnInit } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { PostItemComponent } from '../post-item/post-item.component';

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [CommonModule, PostItemComponent],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css',
})
export class PostListComponent implements OnInit {
  postService: PostService = inject(PostService);
  posts!: Post[];

  ngOnInit(): void {
    this.getPosts();
  }

  getPosts(): void {
    this.postService.getAllPublishedPosts().subscribe((data: Post[]) => {
      this.posts = data;
    });
  }
}
