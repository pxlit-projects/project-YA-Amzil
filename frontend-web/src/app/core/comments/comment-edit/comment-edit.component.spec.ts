import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentEditComponent } from './comment-edit.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { CommentService } from '../../../shared/services/comment.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Comment } from '../../../shared/models/comment.model';

describe('CommentEditComponent', () => {
  let component: CommentEditComponent;
  let fixture: ComponentFixture<CommentEditComponent>;
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
        CommentEditComponent,
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

    fixture = TestBed.createComponent(CommentEditComponent);
    component = fixture.componentInstance;
    commentService = TestBed.inject(CommentService);
    navigateSpy = spyOn(component.router, 'navigate');

    // Mock the history state
    spyOnProperty(history, 'state').and.returnValue({ comment: mockComment });

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with comment data', () => {
    expect(component.updateForm.controls['content'].value).toEqual(
      'Test Content'
    );
  });

  it('should call onSubmit and navigate to read post on valid form', () => {
    const updateCommentSpy = spyOn(
      commentService,
      'updateComment'
    ).and.returnValue(of(mockComment));

    component.updateForm.controls['content'].setValue('Updated Content');
    component.onSubmit();

    expect(updateCommentSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith([
      '/read-post',
      mockComment.postId,
    ]);
  });

  it('should not call onSubmit on invalid form', () => {
    const updateCommentSpy = spyOn(commentService, 'updateComment');

    component.updateForm.controls['content'].setValue('');
    component.onSubmit();

    expect(updateCommentSpy).not.toHaveBeenCalled();
  });

  it('should navigate to read post on cancel', () => {
    component.onCancel();
    expect(navigateSpy).toHaveBeenCalledWith([
      '/read-post',
      mockComment.postId,
    ]);
  });
});
