import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { RouterTestingModule } from '@angular/router/testing';
import { RoleService } from '../../shared/services/role.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  let roleService: RoleService;
  let router: Router;
  let roleSubject: BehaviorSubject<string | null>;

  beforeEach(async () => {
    roleSubject = new BehaviorSubject<string | null>(null);

    await TestBed.configureTestingModule({
      imports: [NavbarComponent, RouterTestingModule, HttpClientTestingModule],
      providers: [
        {
          provide: RoleService,
          useValue: {
            role$: roleSubject.asObservable(),
            setRole: jasmine.createSpy('setRole'),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    roleService = TestBed.inject(RoleService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('should display login link when not logged in', () => {
  //   roleSubject.next(null);
  //   fixture.detectChanges();
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   expect(compiled.querySelector('[routerLink="/login"]')).not.toBeNull();
  // });

  // it('should display logout link when logged in', () => {
  //   roleSubject.next('editor');
  //   fixture.detectChanges();
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   expect(compiled.querySelector('a[click="logout()"]')).not.toBeNull();
  // });

  // it('should display home link when logged in', () => {
  //   roleSubject.next('editor');
  //   fixture.detectChanges();
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   expect(compiled.querySelector('[routerLink="/home"]')).not.toBeNull();
  // });

  // it('should display create post link when logged in as editor', () => {
  //   roleSubject.next('editor');
  //   fixture.detectChanges();
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   expect(
  //     compiled.querySelector('[routerLink="/create-post"]')
  //   ).not.toBeNull();
  // });

  // it('should display dashboard link when logged in as editor', () => {
  //   roleSubject.next('editor');
  //   fixture.detectChanges();
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   expect(compiled.querySelector('[routerLink="/dashboard"]')).not.toBeNull();
  // });

  it('should call logout and navigate to login page', () => {
    spyOn(router, 'navigate');

    component.logout();

    expect(roleService.setRole).toHaveBeenCalledWith(null);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.isLoggedIn).toBeFalse();
    expect(component.role).toBeNull();
    expect(component.currentUser).toBeNull();
  });
});
