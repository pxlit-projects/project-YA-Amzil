import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FilterPostComponent } from './filter-post.component';
import { FormsModule } from '@angular/forms';
import { Filter } from '../../../shared/models/filter.model';

describe('FilterPostComponent', () => {
  let component: FilterPostComponent;
  let fixture: ComponentFixture<FilterPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule, FilterPostComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FilterPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize filter with default values', () => {
    expect(component.filter).toEqual({
      title: '',
      author: '',
      content: '',
      createAt: null,
    });
  });

  it('should emit filterChanged event on form submit with valid data', () => {
    spyOn(component.filterChanged, 'emit');

    component.filter.title = 'Test Title';
    component.filter.author = 'Test Author';
    component.filter.content = 'Test Content';
    const form = {
      valid: true,
      value: { createAt: new Date('2025-01-06') },
    };

    component.onSubmit(form);

    expect(component.filter.title).toBe('test title');
    expect(component.filter.author).toBe('test author');
    expect(component.filter.content).toBe('test content');
    expect(component.filter.createAt).toEqual(new Date('2025-01-06'));
    expect(component.filterChanged.emit).toHaveBeenCalledWith(component.filter);
  });

  it('should not emit filterChanged event on form submit with invalid data', () => {
    spyOn(component.filterChanged, 'emit');

    const form = {
      valid: false,
      value: {},
    };

    component.onSubmit(form);

    expect(component.filterChanged.emit).not.toHaveBeenCalled();
  });

  it('should reset filter and emit filterChanged event on clear', () => {
    spyOn(component.filterChanged, 'emit');

    component.filter.title = 'Test Title';
    component.filter.author = 'Test Author';
    component.filter.content = 'Test Content';
    component.filter.createAt = new Date('2025-01-06');

    component.onClear();

    expect(component.filter).toEqual({
      title: '',
      author: '',
      content: '',
      createAt: null,
    });
    expect(component.filterChanged.emit).toHaveBeenCalledWith(component.filter);
  });
});
