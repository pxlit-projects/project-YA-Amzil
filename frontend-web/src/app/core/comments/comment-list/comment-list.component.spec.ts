import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentListComponent } from './comment-list.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CommentService } from '../../../shared/services/comment.service';
import { of } from 'rxjs';
import { CommentItemComponent } from '../comment-item/comment-item.component';
import { ActivatedRoute } from '@angular/router';
import { Comment } from '../../../shared/models/comment.model';

describe('CommentListComponent', () => {
  let component: CommentListComponent;
  let fixture: ComponentFixture<CommentListComponent>;
  let commentService: CommentService;
  let mockComments: Comment[];

  beforeEach(async () => {
    mockComments = [
      {
        id: 1,
        postId: 1,
        author: 'Author 1',
        content: 'Content 1',
        createAt: new Date(),
        updateAt: new Date(),
      },
      {
        id: 2,
        postId: 1,
        author: 'Author 2',
        content: 'Content 2',
        createAt: new Date(),
        updateAt: new Date(),
      },
    ];

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        CommentListComponent,
        CommentItemComponent,
      ],
      providers: [
        CommentService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { params: { id: '1' } },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CommentListComponent);
    component = fixture.componentInstance;
    commentService = TestBed.inject(CommentService);

    spyOn(commentService, 'getCommentsForPost').and.returnValue(
      of(mockComments)
    );

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load comments on init', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.commentData.length).toBe(2);
    expect(component.commentData).toEqual(mockComments);
  });

  it('should display comment items', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelectorAll('app-comment-item').length).toBe(2);
  });
});
