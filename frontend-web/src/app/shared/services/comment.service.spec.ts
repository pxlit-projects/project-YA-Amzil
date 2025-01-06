import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { CommentService } from './comment.service';
import { Comment } from './../models/comment.model';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;

  const mockComment: Comment = {
    id: 1,
    postId: 1,
    author: 'John Doe',
    content: 'This is a comment.',
    createAt: new Date('2025-01-06'),
    updateAt: new Date('2025-01-06'),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CommentService],
    });

    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create a comment', () => {
    service.createComment(mockComment).subscribe((response) => {
      expect(response).toEqual(mockComment);
    });

    const req = httpMock.expectOne(service['api']);
    expect(req.request.method).toBe('POST');
    req.flush(mockComment);
  });

  it('should get comments for a post', () => {
    const mockComments: Comment[] = [mockComment];

    service.getCommentsForPost(1).subscribe((response) => {
      expect(response).toEqual(mockComments);
    });

    const req = httpMock.expectOne(`${service['api']}/post/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockComments);
  });

  it('should update a comment', () => {
    service.updateComment(mockComment).subscribe((response) => {
      expect(response).toEqual(mockComment);
    });

    const req = httpMock.expectOne(`${service['api']}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockComment);
  });

  it('should delete a comment', () => {
    service.deleteComment(1).subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${service['api']}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
