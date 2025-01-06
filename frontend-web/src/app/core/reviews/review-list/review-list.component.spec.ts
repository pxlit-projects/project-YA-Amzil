import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewListComponent } from './review-list.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReviewService } from '../../../shared/services/review.service';
import { of } from 'rxjs';
import { ReviewItemPublishComponent } from '../review-item-publish/review-item-publish.component';
import { ReviewItemReviseComponent } from '../review-item-revise/review-item-revise.component';
import { Review } from '../../../shared/models/review.model';

describe('ReviewListComponent', () => {
  let component: ReviewListComponent;
  let fixture: ComponentFixture<ReviewListComponent>;
  let reviewService: ReviewService;
  let mockApprovedReviews: Review[];
  let mockRejectedReviews: Review[];

  beforeEach(async () => {
    mockApprovedReviews = [
      { id: 1, postId: 1, reviewer: 'Reviewer 1', comment: 'Approved Review 1', reviewedAt: new Date() },
      { id: 2, postId: 2, reviewer: 'Reviewer 2', comment: 'Approved Review 2', reviewedAt: new Date() }
    ];
    mockRejectedReviews = [
      { id: 3, postId: 3, reviewer: 'Reviewer 3', comment: 'Rejected Review 1', reviewedAt: new Date() },
      { id: 4, postId: 4, reviewer: 'Reviewer 4', comment: 'Rejected Review 2', reviewedAt: new Date() }
    ];

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ReviewListComponent,
        ReviewItemPublishComponent,
        ReviewItemReviseComponent
      ],
      providers: [ReviewService]
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewListComponent);
    component = fixture.componentInstance;
    reviewService = TestBed.inject(ReviewService);

    spyOn(reviewService, 'getAllApprovedPosts').and.returnValue(of(mockApprovedReviews));
    spyOn(reviewService, 'getAllRejectedPosts').and.returnValue(of(mockRejectedReviews));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load approved reviews on init', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.approvedData.length).toBe(2);
    expect(component.approvedData).toEqual(mockApprovedReviews);
  });

  it('should load rejected reviews on init', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.rejectedData.length).toBe(2);
    expect(component.rejectedData).toEqual(mockRejectedReviews);
  });

  it('should display approved review items', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelectorAll('app-review-item-publish').length).toBe(2);
  });

  it('should display rejected review items', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelectorAll('app-review-item-revise').length).toBe(2);
  });
});
