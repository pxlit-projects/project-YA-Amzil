import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreatePostComponent } from './create-post.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { PostService } from '../../../shared/services/post.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { Post } from '../../../shared/models/post.model';

describe('CreatePostComponent', () => {
  let component: CreatePostComponent;
  let fixture: ComponentFixture<CreatePostComponent>;
  let postService: PostService;
  let navigateSpy: jasmine.Spy;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        CreatePostComponent,
      ],
      providers: [PostService],
    }).compileComponents();

    fixture = TestBed.createComponent(CreatePostComponent);
    component = fixture.componentInstance;
    postService = TestBed.inject(PostService);
    navigateSpy = spyOn(component.router, 'navigate');

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form', () => {
    expect(component.postForm).toBeDefined();
  });

  it('should validate form inputs', () => {
    const titleInput = component.postForm.controls['title'];
    const contentInput = component.postForm.controls['content'];
    const authorInput = component.postForm.controls['author'];
    const statusInput = component.postForm.controls['status'];

    titleInput.setValue('');
    contentInput.setValue('');
    authorInput.setValue('');
    statusInput.setValue('');
    expect(component.postForm.valid).toBeFalsy();

    titleInput.setValue('Test Title');
    contentInput.setValue('Test Content');
    authorInput.setValue('Test Author');
    statusInput.setValue('DRAFT');
    expect(component.postForm.valid).toBeTruthy();
  });

  it('should call onSubmit and navigate to dashboard on valid form', () => {
    const createPostSpy = spyOn(postService, 'createPost').and.returnValue(
      of({} as Post)
    );

    component.postForm.controls['title'].setValue('Test Title');
    component.postForm.controls['content'].setValue('Test Content');
    component.postForm.controls['author'].setValue('Test Author');
    component.postForm.controls['status'].setValue('DRAFT');
    component.onSubmit();

    expect(createPostSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should not call onSubmit on invalid form', () => {
    const createPostSpy = spyOn(postService, 'createPost');

    component.postForm.controls['title'].setValue('');
    component.onSubmit();

    expect(createPostSpy).not.toHaveBeenCalled();
  });

  it('should navigate to home on cancel', () => {
    component.onCancel();
    expect(navigateSpy).toHaveBeenCalledWith(['/home']);
  });
});
