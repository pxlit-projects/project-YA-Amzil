import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewItemReviseComponent } from './review-item-revise.component';

describe('ReviewItemReviseComponent', () => {
  let component: ReviewItemReviseComponent;
  let fixture: ComponentFixture<ReviewItemReviseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewItemReviseComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewItemReviseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
