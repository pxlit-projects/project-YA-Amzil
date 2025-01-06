import { TestBed } from '@angular/core/testing';
import { RoleService } from './role.service';

describe('RoleService', () => {
  let service: RoleService;
  let store: { [key: string]: string } = {};

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoleService);

    // Mock localStorage
    spyOn(localStorage, 'getItem').and.callFake(
      (key: string): string | null => {
        return store[key] || null;
      }
    );
    spyOn(localStorage, 'setItem').and.callFake(
      (key: string, value: string): void => {
        store[key] = value;
      }
    );
    spyOn(localStorage, 'removeItem').and.callFake((key: string): void => {
      delete store[key];
    });
  });

  afterEach(() => {
    store = {}; // Clear mock storage after each test
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set role and get role', () => {
    service.setRole('admin');
    expect(localStorage.setItem).toHaveBeenCalledWith('role', 'admin');
    expect(service.getRole()).toBe('admin');
  });

  it('should remove role', () => {
    service.setRole(null);
    expect(localStorage.removeItem).toHaveBeenCalledWith('role');
    expect(service.getRole()).toBeNull();
  });

  it('should set and get current user', () => {
    service.setCurrentUser('testUser');
    expect(localStorage.setItem).toHaveBeenCalledWith(
      'currentUser',
      'testUser'
    );
    expect(service.getCurrentUser()).toBe('testUser');
  });

  it('should remove current user', () => {
    service.setCurrentUser(null);
    expect(localStorage.removeItem).toHaveBeenCalledWith('currentUser');
    expect(service.getCurrentUser()).toBeNull();
  });
});
