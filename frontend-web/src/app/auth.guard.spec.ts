import { TestBed } from '@angular/core/testing';
import { CanActivateFn, Router } from '@angular/router';

import { authGuard } from './auth.guard';
import { RoleService } from './shared/services/role.service';
import { RouterTestingModule } from '@angular/router/testing';

describe('authGuard', () => {
  let mockRoleService: jasmine.SpyObj<RoleService>;
  let mockRouter: jasmine.SpyObj<Router>;

  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => authGuard(...guardParameters));

  beforeEach(() => {
    mockRoleService = jasmine.createSpyObj('RoleService', ['getRole']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        { provide: RoleService, useValue: mockRoleService },
        { provide: Router, useValue: mockRouter },
      ],
    });
  });

  it('should allow the editor to access the create page', () => {
    mockRoleService.getRole.and.returnValue('editor');
    const result = executeGuard({} as any, {} as any);
    expect(result).toBe(true);
  });

  it('should navigate to home page for non-editor roles', () => {
    mockRoleService.getRole.and.returnValue('viewer');
    mockRouter.navigate.and.stub();

    const result = executeGuard({} as any, {} as any);

    expect(result).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/home']);
  });

  it('should navigate to login page if no role is found', () => {
    mockRoleService.getRole.and.returnValue(null);
    mockRouter.navigate.and.stub();

    const result = executeGuard({} as any, {} as any);

    expect(result).toBe(false);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });
});
