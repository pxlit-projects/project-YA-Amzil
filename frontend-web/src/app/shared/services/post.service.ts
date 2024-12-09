import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Post } from '../models/post.model';
import { environment } from '../../../environments/environment.development';
import { Filter } from '../models/filter.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private api: string = environment.apiUrl + 'post/api/post';
  private http: HttpClient = inject(HttpClient);
  postUpdated$: any;

  createPost(post: Post): Observable<Post> {
    return this.http.post<Post>(this.api, post);
  }

  updatePost(post: Post): Observable<Post> {
    return this.http.put<Post>(`${this.api}/${post.id}`, post);
  }

  getAllPublishedPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.api}/published`);
  }

  filterPosts(filter: Filter): Observable<Post[]> {
    return this.getAllPublishedPosts().pipe(
      map((posts: Post[]) =>
        posts.filter((post) => this.isPostMatchingFilter(post, filter))
      )
    );
  }

  private isPostMatchingFilter(post: Post, filter: Filter): boolean {
    const matchesTitle = post.title
      .toLowerCase()
      .includes(filter.title.toLowerCase());
    const matchesAuthor = post.author
      .toLowerCase()
      .includes(filter.author.toLowerCase());
    const matchesContent = post.content
      .toLowerCase()
      .includes(filter.content.toLowerCase());

    const filterCreatedAtDate = filter.createAt
      ? this.normalizeDate(new Date(filter.createAt))
      : null;
    const postCreatedAtDate = this.normalizeDate(new Date(post.createAt));

    const matchesCreatedAt =
      filterCreatedAtDate && !isNaN(filterCreatedAtDate.getTime())
        ? postCreatedAtDate.getTime() === filterCreatedAtDate.getTime()
        : true;

    return matchesTitle && matchesAuthor && matchesContent && matchesCreatedAt;
  }

  private normalizeDate(date: Date): Date {
    if (isNaN(date.getTime())) return date;
    date.setHours(0, 0, 0, 0);
    return date;
  }
}
