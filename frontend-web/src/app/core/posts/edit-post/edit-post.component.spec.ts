import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditPostComponent } from './edit-post.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { PostService } from '../../../shared/services/post.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Post } from '../../../shared/models/post.model';

describe('EditPostComponent', () => {
  let component: EditPostComponent;
  let fixture: ComponentFixture<EditPostComponent>;
  let postService: PostService;
  let navigateSpy: jasmine.Spy;

  const mockPost = {
    id: 1,
    title: 'Test Title',
    content: 'Test Content',
    author: 'Test Author',
    createAt: new Date(),
    updateAt: new Date(),
    status: 'DRAFT',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        EditPostComponent,
      ],
      providers: [
        PostService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => '1', // mock id
              },
            },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(EditPostComponent);
    component = fixture.componentInstance;
    postService = TestBed.inject(PostService);
    navigateSpy = spyOn(component.router, 'navigate');

    // Mock the history state
    spyOnProperty(history, 'state').and.returnValue({ post: mockPost });

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with post data', () => {
    expect(component.updateForm.controls['title'].value).toEqual('Test Title');
    expect(component.updateForm.controls['content'].value).toEqual(
      'Test Content'
    );
    expect(component.updateForm.controls['author'].value).toEqual(
      'Test Author'
    );
    expect(component.updateForm.controls['status'].value).toEqual('DRAFT');
  });

  it('should call onSubmit and navigate to dashboard on valid form', () => {
    const updatePostSpy = spyOn(postService, 'updatePost').and.returnValue(
      of({} as Post)
    );

    component.updateForm.controls['title'].setValue('Updated Title');
    component.updateForm.controls['content'].setValue('Updated Content');
    component.onSubmit();

    expect(updatePostSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should not call onSubmit on invalid form', () => {
    const updatePostSpy = spyOn(postService, 'updatePost');

    component.updateForm.controls['title'].setValue('');
    component.onSubmit();

    expect(updatePostSpy).not.toHaveBeenCalled();
  });

  it('should navigate to home on cancel', () => {
    component.onCancel();
    expect(navigateSpy).toHaveBeenCalledWith(['/home']);
  });
});
