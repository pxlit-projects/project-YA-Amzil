import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RoleService } from '../../shared/services/role.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  selectedRole: string | null = null;

  constructor(private roleService: RoleService, private router: Router) {}

  onRoleChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedRole = selectElement.value;
    console.log('Selected role:', this.selectedRole);
  }

  onSubmit(event: Event): void {
    event.preventDefault();
    if (this.selectedRole && this.selectedRole !== '') {
      this.roleService.setRole(this.selectedRole);
      console.log('Role submitted:', this.selectedRole);
      // Stuur altijd naar de homepagina
      this.router.navigate(['/home']);
    } else {
      console.error('No role selected');
    }
  }
}
