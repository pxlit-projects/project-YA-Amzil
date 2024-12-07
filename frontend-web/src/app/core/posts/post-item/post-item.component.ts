import { Component, Input } from '@angular/core';
import { Post } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-item.component.html',
  styleUrl: './post-item.component.css',
})
export class PostItemComponent   {
 @Input() post!: Post;
}
