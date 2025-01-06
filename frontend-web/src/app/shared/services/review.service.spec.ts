import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ReviewService } from './review.service';
import { Review } from './../models/review.model';

describe('ReviewService', () => {
  let service: ReviewService;
  let httpMock: HttpTestingController;

  const mockReview: Review = {
    postId: 1,
    reviewer: 'John Doe',
    comment: 'Great post!',
    reviewedAt: new Date(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewService],
    });

    service = TestBed.inject(ReviewService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should approve review', () => {
    service.approveReview(1).subscribe((response) => {
      expect(response).toEqual(mockReview);
    });

    const req = httpMock.expectOne(`${service['api']}/approve/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockReview);
  });

  it('should reject review', () => {
    service.rejectReview(mockReview).subscribe((response) => {
      expect(response).toEqual(mockReview);
    });

    const req = httpMock.expectOne(`${service['api']}/reject/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockReview);
  });

  it('should get all approved posts', () => {
    const mockReviews: Review[] = [mockReview];

    service.getAllApprovedPosts().subscribe((response) => {
      expect(response).toEqual(mockReviews);
    });

    const req = httpMock.expectOne(`${service['api']}/approved`);
    expect(req.request.method).toBe('GET');
    req.flush(mockReviews);
  });

  it('should get all rejected posts', () => {
    const mockReviews: Review[] = [mockReview];

    service.getAllRejectedPosts().subscribe((response) => {
      expect(response).toEqual(mockReviews);
    });

    const req = httpMock.expectOne(`${service['api']}/rejected`);
    expect(req.request.method).toBe('GET');
    req.flush(mockReviews);
  });

  it('should publish post', () => {
    service.publishPost(1).subscribe((response) => {
      expect(response).toEqual(mockReview);
    });

    const req = httpMock.expectOne(`${service['api']}/publish/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockReview);
  });

  it('should revise post', () => {
    service.revisePost(1).subscribe((response) => {
      expect(response).toEqual(mockReview);
    });

    const req = httpMock.expectOne(`${service['api']}/revise/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockReview);
  });
});
