import { Component, inject, Input, OnInit } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { CommentListComponent } from "../../comments/comment-list/comment-list.component";

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule, CommentListComponent],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css',
})
export class PostDetailComponent implements OnInit {
  postService: PostService = inject(PostService);
  route: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  post!: Post;

  ngOnInit(): void {
    const postId = this.route.snapshot.params['id'];
    this.postService.getPostById(postId).subscribe((data: Post) => {
      this.post = data;
    });
  }
}
