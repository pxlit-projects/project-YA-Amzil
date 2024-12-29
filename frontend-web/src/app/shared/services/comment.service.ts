import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.model';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private api: string = environment.apiUrl + 'comment/api/comment';
  private http: HttpClient = inject(HttpClient);

  createComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.api, comment);
  }

  getCommentsForPost(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.api}/post/${postId}`);
  }

  updateComment(comment: Comment): Observable<Comment> {
    return this.http.put<Comment>(`${this.api}/${comment.id}`, comment);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${commentId}`);
  }
}
