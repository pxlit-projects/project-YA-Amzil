import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostDashboardComponent } from './post-dashboard.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { PostService } from '../../../shared/services/post.service';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { FilterPostComponent } from '../filter-post/filter-post.component';
import { PostItemDashboardComponent } from '../post-item-dashboard/post-item-dashboard.component';
import { Post } from '../../../shared/models/post.model';
import { Filter } from '../../../shared/models/filter.model';

describe('PostDashboardComponent', () => {
  let component: PostDashboardComponent;
  let fixture: ComponentFixture<PostDashboardComponent>;
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
        PostDashboardComponent,
        HttpClientTestingModule,
        RouterTestingModule,
        FilterPostComponent,
        PostItemDashboardComponent,
      ],
      providers: [PostService],
    }).compileComponents();

    fixture = TestBed.createComponent(PostDashboardComponent);
    component = fixture.componentInstance;
    postService = TestBed.inject(PostService);

    spyOn(postService, 'getAllDraftPosts').and.returnValue(of(mockPosts));
    spyOn(postService, 'getAllPendingPosts').and.returnValue(of(mockPosts));
    spyOn(postService, 'filterDraftPosts').and.returnValue(of(mockPosts));
    spyOn(postService, 'filterPendingPosts').and.returnValue(of(mockPosts));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load all draft posts on init', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.filteredDraftData.length).toBe(2);
  });

  it('should load all pending posts on init', () => {
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.filteredPendingData.length).toBe(2);
  });

  it('should filter draft posts based on filter criteria', () => {
    const filter: Filter = {
      title: 'Post 1',
      author: '',
      content: '',
      createAt: null,
    };
    component.handleFilterDraft(filter);
    fixture.detectChanges();
    expect(component.filteredDraftData.length).toBe(2);
  });

  it('should filter pending posts based on filter criteria', () => {
    const filter: Filter = {
      title: 'Post 1',
      author: '',
      content: '',
      createAt: null,
    };
    component.handleFilterPending(filter);
    fixture.detectChanges();
    expect(component.filteredPendingData.length).toBe(2);
  });
});
