import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewItemPublishComponent } from './review-item-publish.component';
import { RouterTestingModule } from '@angular/router/testing';
import { CommonModule } from '@angular/common';
import { ReviewService } from '../../../shared/services/review.service';
import { RoleService } from '../../../shared/services/role.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { Review } from '../../../shared/models/review.model';

describe('ReviewItemPublishComponent', () => {
  let component: ReviewItemPublishComponent;
  let fixture: ComponentFixture<ReviewItemPublishComponent>;
  let reviewService: ReviewService;
  let mockReview: Review;
  let navigateSpy: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CommonModule,
        RouterTestingModule,
        HttpClientTestingModule,
        ReviewItemPublishComponent,
      ],
      providers: [ReviewService, RoleService],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewItemPublishComponent);
    component = fixture.componentInstance;
    reviewService = TestBed.inject(ReviewService);
    navigateSpy = spyOn(component.router, 'navigate');

    mockReview = {
      id: 1,
      postId: 1,
      reviewer: 'Test Reviewer',
      comment: 'Test Comment',
      reviewedAt: new Date(),
    };

    component.review = mockReview;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('should call onPublish and navigate to home on publish click', () => {
  //   spyOn(reviewService, 'publishPost').and.returnValue(of(mockReview));

  //   component.onPublish(mockReview.postId);
  //   expect(reviewService.publishPost).toHaveBeenCalledWith(mockReview.postId);
  //   expect(navigateSpy).toHaveBeenCalledWith(['/home']);
  // });
});
