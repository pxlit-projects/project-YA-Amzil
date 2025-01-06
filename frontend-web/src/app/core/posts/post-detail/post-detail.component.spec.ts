import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostDetailComponent } from './post-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { PostService } from '../../../shared/services/post.service';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Post } from '../../../shared/models/post.model';
import { CommentListComponent } from '../../comments/comment-list/comment-list.component';
import { CommonModule } from '@angular/common';

describe('PostDetailComponent', () => {
  let component: PostDetailComponent;
  let fixture: ComponentFixture<PostDetailComponent>;
  let postService: PostService;

  const mockPost: Post = {
    id: 1,
    title: 'Test Post',
    content: 'Test Content',
    author: 'Test Author',
    createAt: new Date(),
    updateAt: new Date(),
    status: 'PUBLISHED',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        PostDetailComponent,
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        CommonModule,
        CommentListComponent,
      ],
      providers: [
        PostService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { params: { id: '1' } },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PostDetailComponent);
    component = fixture.componentInstance;
    postService = TestBed.inject(PostService);

    spyOn(postService, 'getPostById').and.returnValue(of(mockPost));

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
