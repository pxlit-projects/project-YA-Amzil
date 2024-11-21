import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelevantPostComponent } from './relevant-post.component';

describe('RelevantPostComponent', () => {
  let component: RelevantPostComponent;
  let fixture: ComponentFixture<RelevantPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RelevantPostComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RelevantPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
