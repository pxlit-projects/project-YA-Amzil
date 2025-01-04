import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private roleSubject = new BehaviorSubject<string | null>(
    localStorage.getItem('role')
  );
  role$ = this.roleSubject.asObservable();
  private currentUserSubject = new BehaviorSubject<string | null>(
    localStorage.getItem('currentUser')
  );
  currentUser$ = this.currentUserSubject.asObservable();

  constructor() {}

  setRole(role: string | null): void {
    if (role) {
      localStorage.setItem('role', role);
    } else {
      localStorage.removeItem('role');
    }
    this.roleSubject.next(role);
    console.log('Role is set to', role);
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  setCurrentUser(user: string | null): void {
    if (user) {
      localStorage.setItem('currentUser', user);
    } else {
      localStorage.removeItem('currentUser');
    }
    this.currentUserSubject.next(user);
    console.log('Current user is set to', user);
  }

  getCurrentUser(): string | null {
    return localStorage.getItem('currentUser');
  }
}

