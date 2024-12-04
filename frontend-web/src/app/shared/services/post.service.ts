import { inject, Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../models/post.model';
import { environment } from '../../../environments/environment.development';
import { Filter } from '../models/filter.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private api: string = environment.apiUrl + 'post/api/post';
  private http: HttpClient = inject(HttpClient);

  createPost(post: Post): Observable<Post> {
    return this.http.post<Post>(this.api, post);
  }

  // updatePost(post: Post): Observable<Post> {
  //   return this.http.put<Post>(`${this.api}/${post.id}`, post);
  // }

  // getRelevantPosts(filter: Filter): Observable<Post[]> {
  //   let params = new HttpParams();
  //   if (filter.content) {
  //     params = params.append('content', filter.content.toLowerCase());
  //   }
  //   if (filter.category) {
  //     params = params.append('category', filter.category.toLowerCase());
  //   }
  //   if (filter.author) {
  //     params = params.append('author', filter.author.toLowerCase());
  //   }
  //   return this.http.get<Post[]>(`${this.api}/filter`, { params });
  // }
}
