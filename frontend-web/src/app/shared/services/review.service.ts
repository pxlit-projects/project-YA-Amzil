import { Review } from './../models/review.model';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private api: string = environment.apiUrl + 'review/api/review';
  private http: HttpClient = inject(HttpClient);

  approveReview(postId: number): Observable<Review> {
    return this.http.post<Review>(`${this.api}/approve/${postId}`, {});
  }

  rejectReview(review: Review): Observable<Review> {
    return this.http.post<Review>(`${this.api}/reject/${review.postId}`, review);
  }

  getAllApprovedPosts(): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.api}/approved`);
  }

  getAllRejectedPosts(): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.api}/rejected`);
  }

  publishPost(postId: number): Observable<Review> {
    return this.http.put<Review>(`${this.api}/publish/${postId}`, {});
  }

  revisePost(postId: number): Observable<Review> {
    return this.http.put<Review>(`${this.api}/revise/${postId}`, {});
  }
}

