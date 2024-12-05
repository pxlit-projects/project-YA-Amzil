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

  constructor() {}

  setRole(role: string): void {
    localStorage.setItem('role', role);
    this.roleSubject.next(role);
    console.log('Role is set to', role);
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }
}
