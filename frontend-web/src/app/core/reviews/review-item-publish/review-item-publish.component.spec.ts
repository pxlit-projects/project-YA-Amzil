import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewItemPublishComponent } from './review-item-publish.component';

describe('ReviewItemPublishComponent', () => {
  let component: ReviewItemPublishComponent;
  let fixture: ComponentFixture<ReviewItemPublishComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewItemPublishComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewItemPublishComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
