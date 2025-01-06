import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostItemComponent } from './post-item.component';
import { RouterTestingModule } from '@angular/router/testing';
import { CommonModule } from '@angular/common';
import { By } from '@angular/platform-browser';
import { Post } from '../../../shared/models/post.model';
import { RoleService } from '../../../shared/services/role.service';

describe('PostItemComponent', () => {
  let component: PostItemComponent;
  let fixture: ComponentFixture<PostItemComponent>;
  let roleService: RoleService;
  let mockPost: Post;
  let navigateSpy: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommonModule, RouterTestingModule, PostItemComponent],
      providers: [RoleService],
    }).compileComponents();

    fixture = TestBed.createComponent(PostItemComponent);
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

  it('should navigate to comment post on comment click', () => {
    component.onComment(mockPost);
    expect(navigateSpy).toHaveBeenCalledWith(['/comment-post', mockPost.id]);
  });

  it('should navigate to read post on read more click', () => {
    component.onReadmore(mockPost);
    expect(navigateSpy).toHaveBeenCalledWith(['/read-post', mockPost.id]);
  });
});
