import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private api: string = environment.apiUrl + 'comment/api/comment';
  private http: HttpClient = inject(HttpClient);

  createComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.api, comment);
  }

  // deleteComment(commentId: number): Observable<Comment> {
  //   return this.http.delete<Comment>(`${this.api}/${commentId}`);
  // }
}


