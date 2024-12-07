import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Filter } from '../../../shared/models/filter.model';
import { Post } from '../../../shared/models/post.model';
import { PostService } from '../../../shared/services/post.service';

@Component({
  selector: 'app-filter-post',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './filter-post.component.html',
  styleUrl: './filter-post.component.css',
})
export class FilterPostComponent {
  filter: Filter = { title: '', author: '', content: '', createAt: null };
  // posts: Post[] = [];

  @Output() filterChanged = new EventEmitter<Filter>();

  // constructor(private postService: PostService) {}

  onSubmit(form: any) {
    if (form.valid) {
      this.filter.title = this.filter.title.toLowerCase();
      this.filter.author = this.filter.author.toLowerCase();
      this.filter.content = this.filter.content.toLowerCase();

      this.filter.createAt = form.value.createAt;
      this.filterChanged.emit(this.filter);
      // this.postService.getRelevantPosts(this.filter).subscribe((posts) => {
      //   this.posts = posts;
      // });
    }
  }
}
