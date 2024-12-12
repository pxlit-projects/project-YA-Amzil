import { Component, inject } from '@angular/core';
import { PostItemComponent } from "../post-item/post-item.component";
import { Post } from '../../../shared/models/post.model';
import { PostService } from '../../../shared/services/post.service';
import { Filter } from '../../../shared/models/filter.model';
import { FilterPostComponent } from "../filter-post/filter-post.component";

@Component({
  selector: 'app-post-dashboard',
  standalone: true,
  imports: [PostItemComponent, FilterPostComponent],
  templateUrl: './post-dashboard.component.html',
  styleUrl: './post-dashboard.component.css',
})
export class PostDashboardComponent {
  posts!: Post[];
  filteredData!: Post[];
  postService: PostService = inject(PostService);

  ngOnInit(): void {
    this.getPosts();
  }

  handleFilter(filter: Filter) {
    this.postService.filterDraftAndPendingPosts(filter).subscribe({
      next: (posts) => {
        this.filteredData = posts;
        posts;
        console.log('Filtered Posts:', this.filteredData);
      },
    });
  }

  getPosts() {
    this.postService.getAllDraftAndPendingPosts().subscribe({
      next: (posts) => {
        this.filteredData = posts;
      },
    });
  }
}
