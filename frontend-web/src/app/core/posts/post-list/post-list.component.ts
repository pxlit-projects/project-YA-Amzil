import { Component, inject, OnInit } from '@angular/core';
import { PostService } from '../../../shared/services/post.service';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { PostItemComponent } from '../post-item/post-item.component';
import { FilterPostComponent } from "../filter-post/filter-post.component";
import { Filter } from '../../../shared/models/filter.model';

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [CommonModule, PostItemComponent, FilterPostComponent],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.css',
})
export class PostListComponent implements OnInit {
  posts!: Post[];
  filteredData!: Post[];
  postService: PostService = inject(PostService);

  ngOnInit(): void {
    this.getPosts();
  }

  handleFilter(filter: Filter) {
    this.postService.filterPublishedPosts(filter).subscribe({
      next: (posts) => {
        this.filteredData = posts;
        posts;
        console.log('Filtered Posts:', this.filteredData);
      },
    });
  }

  getPosts() {
    this.postService.getAllPublishedPosts().subscribe({
      next: (posts) => {
        // this.posts = posts; // --> Hier krijg mij lijst van posts te zien
        this.filteredData = posts; // --> Hier krijg ik de gefilterde lijst te zien
      },
    });
  }
}
