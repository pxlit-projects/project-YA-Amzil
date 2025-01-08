import { Component, inject, OnInit } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { Post } from '../../../shared/models/post.model';
import { PostItemComponent } from '../post-item/post-item.component';
import { FilterPostComponent } from '../filter-post/filter-post.component';
import { Filter } from '../../../shared/models/filter.model';

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [PostItemComponent, FilterPostComponent],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css',
})
export class PostListComponent implements OnInit {
  posts!: Post[];
  filteredPublishedData!: Post[];
  postService: PostService = inject(PostService);

  ngOnInit(): void {
    this.getPosts();
  }

  handleFilter(filter: Filter) {
    this.postService.filterPublishedPosts(filter).subscribe({
      next: (posts) => (this.filteredPublishedData = posts),
      error: (err) => {
        console.error('Error filtering published posts:', err);
        this.filteredPublishedData = [];
      },
    });
  }

  getPosts() {
    this.postService.getAllPublishedPosts().subscribe({
      next: (posts) => (this.filteredPublishedData = posts),
      error: (err) => {
        console.error('Error fetching published posts:', err);
        this.filteredPublishedData = [];
      },
    });
  }
}
