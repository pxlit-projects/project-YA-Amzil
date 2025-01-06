import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { PostService } from './post.service';
import { Post } from './../models/post.model';
import { Filter } from './../models/filter.model';
import { of } from 'rxjs';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;

  const mockPost: Post = {
    id: 1,
    title: 'Sample Post',
    content: 'This is a sample post.',
    author: 'Author Name',
    createAt: new Date('2025-01-06'),
    updateAt: new Date('2025-01-06'),
    status: 'DRAFT',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PostService],
    });

    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create a post', () => {
    service.createPost(mockPost).subscribe((response) => {
      expect(response).toEqual(mockPost);
    });

    const req = httpMock.expectOne(service['api']);
    expect(req.request.method).toBe('POST');
    req.flush(mockPost);
  });

  it('should update a post', () => {
    service.updatePost(mockPost).subscribe((response) => {
      expect(response).toEqual(mockPost);
    });

    const req = httpMock.expectOne(`${service['api']}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockPost);
  });

  it('should get a post by id', () => {
    service.getPostById(1).subscribe((response) => {
      expect(response).toEqual(mockPost);
    });

    const req = httpMock.expectOne(`${service['api']}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPost);
  });

  it('should get all draft posts', () => {
    const mockPosts: Post[] = [mockPost];

    service.getAllDraftPosts().subscribe((response) => {
      expect(response).toEqual(mockPosts);
    });

    const req = httpMock.expectOne(`${service['api']}/draft`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);
  });

  it('should get all pending posts', () => {
    const mockPosts: Post[] = [mockPost];

    service.getAllPendingPosts().subscribe((response) => {
      expect(response).toEqual(mockPosts);
    });

    const req = httpMock.expectOne(`${service['api']}/pending`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);
  });

  it('should get all published posts', () => {
    const mockPosts: Post[] = [mockPost];

    service.getAllPublishedPosts().subscribe((response) => {
      expect(response).toEqual(mockPosts);
    });

    const req = httpMock.expectOne(`${service['api']}/published`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);
  });

  it('should get all draft and pending posts', () => {
    const mockPosts: Post[] = [mockPost];

    service.getAllDraftAndPendingPosts().subscribe((response) => {
      expect(response).toEqual(mockPosts);
    });

    const req = httpMock.expectOne(`${service['api']}/draft-pending`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);
  });

  it('should filter draft posts', () => {
    const filter: Filter = {
      title: 'Sample',
      author: 'Author',
      content: 'sample',
      createAt: new Date('2025-01-06'),
    };

    spyOn(service, 'getAllDraftPosts').and.returnValue(of([mockPost]));

    service.filterDraftPosts(filter).subscribe((response) => {
      expect(response).toEqual([mockPost]);
    });
  });

  it('should filter pending posts', () => {
    const filter: Filter = {
      title: 'Sample',
      author: 'Author',
      content: 'sample',
      createAt: new Date('2025-01-06'),
    };

    spyOn(service, 'getAllPendingPosts').and.returnValue(of([mockPost]));

    service.filterPendingPosts(filter).subscribe((response) => {
      expect(response).toEqual([mockPost]);
    });
  });

  it('should filter published posts', () => {
    const filter: Filter = {
      title: 'Sample',
      author: 'Author',
      content: 'sample',
      createAt: new Date('2025-01-06'),
    };

    spyOn(service, 'getAllPublishedPosts').and.returnValue(of([mockPost]));

    service.filterPublishedPosts(filter).subscribe((response) => {
      expect(response).toEqual([mockPost]);
    });
  });

  it('should filter draft and pending posts', () => {
    const filter: Filter = {
      title: 'Sample',
      author: 'Author',
      content: 'sample',
      createAt: new Date('2025-01-06'),
    };

    spyOn(service, 'getAllDraftAndPendingPosts').and.returnValue(
      of([mockPost])
    );

    service.filterDraftAndPendingPosts(filter).subscribe((response) => {
      expect(response).toEqual([mockPost]);
    });
  });
});
