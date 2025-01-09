import { Component, inject, OnInit } from '@angular/core';
import { Post } from '../../../shared/models/post.model';
import { PostService } from '../../../shared/services/post.service';
import { Filter } from '../../../shared/models/filter.model';
import { FilterPostComponent } from '../filter-post/filter-post.component';
import { PostItemDashboardComponent } from "../post-item-dashboard/post-item-dashboard.component";

@Component({
  selector: 'app-post-dashboard',
  standalone: true,
  imports: [FilterPostComponent, PostItemDashboardComponent],
  templateUrl: './post-dashboard.component.html',
  styleUrls: ['./post-dashboard.component.css'],
})
export class PostDashboardComponent implements OnInit {
  posts!: Post[];
  filteredDraftData!: Post[];
  filteredPendingData!: Post[];

  postService: PostService = inject(PostService);

  ngOnInit(): void {
    this.getDraftPosts();
    this.getPendingPosts();
  }

  getDraftPosts() {
    this.postService.getAllDraftPosts().subscribe({
      next: (posts) => {
        this.filteredDraftData = posts;
      },
      error: (err) => {
        console.error('Error fetching draft posts:', err);
        this.filteredDraftData = [];
      },
    });
  }

  getPendingPosts() {
    this.postService.getAllPendingPosts().subscribe({
      next: (posts) => {
        this.filteredPendingData = posts;
      },
      error: (err) => {
        console.error('Error fetching pending posts:', err);
        this.filteredPendingData = [];
      },
    });
  }

  handleFilterDraft(filter: Filter) {
    this.postService.filterDraftPosts(filter).subscribe({
      next: (posts) => {
        this.filteredDraftData = posts;
        console.log('Filtered Draft Posts:', this.filteredDraftData);
      },
      error: (err) => {
        console.error('Error filtering draft posts:', err);
      },
    });
  }

  handleFilterPending(filter: Filter) {
    this.postService.filterPendingPosts(filter).subscribe({
      next: (posts) => {
        this.filteredPendingData = posts;
        console.log('Filtered Pending Posts:', this.filteredPendingData);
      },
      error: (err) => {
        console.error('Error filtering pending posts:', err);
      },
    });
  }
}
