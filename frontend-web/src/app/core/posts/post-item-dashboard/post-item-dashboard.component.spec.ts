import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostItemDashboardComponent } from './post-item-dashboard.component';
import { RouterTestingModule } from '@angular/router/testing';
import { CommonModule } from '@angular/common';
import { By } from '@angular/platform-browser';
import { Post } from '../../../shared/models/post.model';
import { RoleService } from '../../../shared/services/role.service';

describe('PostItemDashboardComponent', () => {
  let component: PostItemDashboardComponent;
  let fixture: ComponentFixture<PostItemDashboardComponent>;
  let roleService: RoleService;
  let mockPost: Post;
  let navigateSpy: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommonModule, RouterTestingModule, PostItemDashboardComponent],
      providers: [RoleService],
    }).compileComponents();

    fixture = TestBed.createComponent(PostItemDashboardComponent);
    component = fixture.componentInstance;
    roleService = TestBed.inject(RoleService);
    navigateSpy = spyOn(component.router, 'navigate');

    mockPost = {
      id: 1,
      title: 'Test Post',
      content: 'Test Content',
      author: 'Test Author',
      createAt: new Date(),
      updateAt: new Date(),
      status: 'PUBLISHED',
    };

    component.post = mockPost;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to edit post on edit click', () => {
    component.onEdit(mockPost);
    expect(navigateSpy).toHaveBeenCalledWith(['/post-edit', mockPost.id], {
      state: { post: mockPost },
    });
  });

  it('should navigate to review post on review click', () => {
    component.onReview(mockPost);
    expect(navigateSpy).toHaveBeenCalledWith(['/review-post', mockPost.id]);
  });
});
