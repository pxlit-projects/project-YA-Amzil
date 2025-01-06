import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { LoginComponent } from './login.component';
import { RoleService } from '../../shared/services/role.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let roleService: RoleService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        LoginComponent,
      ],
      providers: [RoleService],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    roleService = TestBed.inject(RoleService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form', () => {
    expect(component.roleForm).toBeDefined();
  });

  it('should validate form inputs', () => {
    const nameInput = component.roleForm.controls['name'];
    const roleInput = component.roleForm.controls['role'];

    nameInput.setValue('');
    roleInput.setValue('');
    expect(component.roleForm.valid).toBeFalsy();

    nameInput.setValue('Test User');
    roleInput.setValue('admin');
    expect(component.roleForm.valid).toBeTruthy();
  });

  it('should call onSubmit and navigate to home on valid form', () => {
    const navigateSpy = spyOn(router, 'navigate');
    const setRoleSpy = spyOn(roleService, 'setRole');
    const setCurrentUserSpy = spyOn(roleService, 'setCurrentUser');

    component.roleForm.controls['name'].setValue('Test User');
    component.roleForm.controls['role'].setValue('admin');
    component.onSubmit(new Event('submit'));

    expect(setRoleSpy).toHaveBeenCalledWith('admin');
    expect(setCurrentUserSpy).toHaveBeenCalledWith('Test User');
    expect(navigateSpy).toHaveBeenCalledWith(['/home']);
  });

  it('should display error message on invalid form', () => {
    spyOn(console, 'error');
    component.roleForm.controls['name'].setValue('');
    component.roleForm.controls['role'].setValue('');
    component.onSubmit(new Event('submit'));
    expect(console.error).toHaveBeenCalledWith('Form is not valid');
  });
});
