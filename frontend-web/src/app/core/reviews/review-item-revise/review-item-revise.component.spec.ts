import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewItemReviseComponent } from './review-item-revise.component';
import { CommonModule } from '@angular/common';
import { Review } from '../../../shared/models/review.model';
import { ReviewService } from '../../../shared/services/review.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

describe('ReviewItemReviseComponent', () => {
  let component: ReviewItemReviseComponent;
  let fixture: ComponentFixture<ReviewItemReviseComponent>;
  let reviewService: ReviewService;
  let mockReview: Review;
  let navigateSpy: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CommonModule,
        HttpClientTestingModule,
        ReviewItemReviseComponent,
      ],
      providers: [ReviewService],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewItemReviseComponent);
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

  // it('should call onRevise and navigate to dashboard on revise click', () => {
  //   spyOn(reviewService, 'revisePost').and.returnValue(of(mockReview));

  //   component.onRevise(mockReview.postId);
  //   expect(reviewService.revisePost).toHaveBeenCalledWith(mockReview.postId);
  //   expect(navigateSpy).toHaveBeenCalledWith(['/dashboard']);
  // });
});
