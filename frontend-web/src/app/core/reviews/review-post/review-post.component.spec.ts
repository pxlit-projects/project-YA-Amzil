import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewPostComponent } from './review-post.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { PostService } from '../../../shared/services/post.service';
import { ReviewService } from '../../../shared/services/review.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Post } from '../../../shared/models/post.model';
import { Review } from '../../../shared/models/review.model';

describe('ReviewPostComponent', () => {
  let component: ReviewPostComponent;
  let fixture: ComponentFixture<ReviewPostComponent>;
  let postService: PostService;
  let reviewService: ReviewService;
  let navigateSpy: jasmine.Spy;

  const mockPost: Post = {
    id: 1,
    title: 'Test Post',
    content: 'Test Content',
    author: 'Test Author',
    createAt: new Date(),
    updateAt: new Date(),
    status: 'PENDING',
  };

  const mockReview: Review = {
    id: 1,
    postId: 1,
    reviewer: 'Test Reviewer',
    comment: 'Test Comment',
    reviewedAt: new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        ReviewPostComponent,
      ],
      providers: [
        PostService,
        ReviewService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { params: { id: '1' } },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewPostComponent);
    component = fixture.componentInstance;
    postService = TestBed.inject(PostService);
    reviewService = TestBed.inject(ReviewService);
    navigateSpy = spyOn(component.router, 'navigate');

    spyOn(postService, 'getPostById').and.returnValue(of(mockPost));
    spyOn(reviewService, 'approveReview').and.returnValue(of(mockReview));
    spyOn(reviewService, 'rejectReview').and.returnValue(of(mockReview));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load post on init', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.post).toEqual(mockPost);
  });
});
