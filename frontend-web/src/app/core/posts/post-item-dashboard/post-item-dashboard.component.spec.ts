import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostItemDashboardComponent } from './post-item-dashboard.component';

describe('PostItemDashboardComponent', () => {
  let component: PostItemDashboardComponent;
  let fixture: ComponentFixture<PostItemDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostItemDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostItemDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
