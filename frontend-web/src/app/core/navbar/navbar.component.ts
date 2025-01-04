import { Router, RouterLink } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { RoleService } from '../../shared/services/role.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent implements OnInit {
  isLoggedIn: boolean = false;
  role: string | null = null;
  currentUser: string | null = null;

  constructor(private roleService: RoleService, private router: Router) {}

  ngOnInit(): void {
    this.roleService.role$.subscribe((role) => {
      this.role = role;
      this.isLoggedIn = !!role;
      // this.currentUser = this.roleService.getCurrentUser();
    });
  }

  logout(): void {
    this.roleService.setRole(null);
    this.isLoggedIn = false;
    this.role = null;
    this.currentUser = null;
    this.router.navigate(['/login']);
  }
}
