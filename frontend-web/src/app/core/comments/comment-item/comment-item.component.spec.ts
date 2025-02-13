import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentItemComponent } from './comment-item.component';
import { CommonModule } from '@angular/common';
import { By } from '@angular/platform-browser';
import { Comment } from '../../../shared/models/comment.model';
import { CommentService } from './../../../shared/services/comment.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

describe('CommentItemComponent', () => {
  let component: CommentItemComponent;
  let fixture: ComponentFixture<CommentItemComponent>;
  let commentService: CommentService;
  let mockComment: Comment;
  let navigateSpy: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CommonModule,
        HttpClientTestingModule,
        CommentItemComponent,
      ],
      providers: [CommentService],
    }).compileComponents();

    fixture = TestBed.createComponent(CommentItemComponent);
    component = fixture.componentInstance;
    commentService = TestBed.inject(CommentService);
    navigateSpy = spyOn(component.router, 'navigate');

    mockComment = {
      id: 1,
      postId: 1,
      author: 'Test Author',
      content: 'Test Content',
      createAt: new Date(),
      updateAt: new Date(),
    };

    component.comment = mockComment;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to edit comment on edit click', () => {
    component.onEdit(mockComment);
    expect(navigateSpy).toHaveBeenCalledWith(
      ['/comment-edit', mockComment.id],
      { state: { comment: mockComment } }
    );
  });
});
