import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentPostComponent } from './comment-post.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { CommentService } from '../../../shared/services/comment.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Comment } from '../../../shared/models/comment.model';

describe('CommentPostComponent', () => {
  let component: CommentPostComponent;
  let fixture: ComponentFixture<CommentPostComponent>;
  let commentService: CommentService;
  let navigateSpy: jasmine.Spy;

  const mockComment: Comment = {
    id: 1,
    postId: 1,
    author: 'Test Author',
    content: 'Test Content',
    createAt: new Date(),
    updateAt: new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        CommentPostComponent,
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

    fixture = TestBed.createComponent(CommentPostComponent);
    component = fixture.componentInstance;
    commentService = TestBed.inject(CommentService);
    navigateSpy = spyOn(component.router, 'navigate');

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with postId', () => {
    expect(component.commentForm.controls['postId'].value).toEqual('1');
  });

  it('should call onSubmit and navigate to read post on valid form', () => {
    const createCommentSpy = spyOn(
      commentService,
      'createComment'
    ).and.returnValue(of(mockComment));

    component.commentForm.controls['author'].setValue('Test Author');
    component.commentForm.controls['content'].setValue('Test Content');
    component.onSubmit();

    expect(createCommentSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['read-post', '1']);
  });

  it('should not call onSubmit on invalid form', () => {
    const createCommentSpy = spyOn(commentService, 'createComment');

    component.commentForm.controls['author'].setValue('');
    component.onSubmit();

    expect(createCommentSpy).not.toHaveBeenCalled();
  });

  it('should navigate to read post on cancel', () => {
    component.onCancel();
    expect(navigateSpy).toHaveBeenCalledWith(['read-post', '1']);
  });
});
