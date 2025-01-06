import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostListComponent } from './post-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { PostService } from '../../../shared/services/post.service';
import { of } from 'rxjs';
import { PostItemComponent } from '../post-item/post-item.component';
import { FilterPostComponent } from '../filter-post/filter-post.component';
import { Post } from '../../../shared/models/post.model';
import { Filter } from '../../../shared/models/filter.model';

describe('PostListComponent', () => {
  let component: PostListComponent;
  let fixture: ComponentFixture<PostListComponent>;
  let postService: PostService;

  const mockPosts: Post[] = [
    {
      id: 1,
      title: 'Post 1',
      content: 'Content 1',
      author: 'Author 1',
      createAt: new Date(),
      updateAt: new Date(),
      status: 'PUBLISHED',
    },
    {
      id: 2,
      title: 'Post 2',
      content: 'Content 2',
      author: 'Author 2',
      createAt: new Date(),
      updateAt: new Date(),
      status: 'DRAFT',
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        PostListComponent,
        HttpClientTestingModule,
        RouterTestingModule,
        PostItemComponent,
        FilterPostComponent,
      ],
      providers: [PostService],
    }).compileComponents();

    fixture = TestBed.createComponent(PostListComponent);
    component = fixture.componentInstance;
    postService = TestBed.inject(PostService);

    spyOn(postService, 'getAllPublishedPosts').and.returnValue(of(mockPosts));
    spyOn(postService, 'filterPublishedPosts').and.returnValue(of(mockPosts));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load all published posts on init', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.filteredPublishedData.length).toBe(2);
  });

  it('should filter published posts based on filter criteria', () => {
    const filter: Filter = {
      title: 'Post 1',
      author: '',
      content: '',
      createAt: null,
    };
    component.handleFilter(filter);
    fixture.detectChanges();
    expect(component.filteredPublishedData.length).toBe(2);
  });

  it('should display post items', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelectorAll('app-post-item').length).toBe(2);
  });
});
