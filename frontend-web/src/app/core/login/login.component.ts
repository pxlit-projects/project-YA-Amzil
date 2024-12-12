import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { RoleService } from '../../shared/services/role.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  selectedRole: string | null = null;
  fb: FormBuilder = inject(FormBuilder);


  roleForm: FormGroup = this.fb.group({
      role: ['', [Validators.required]],
  });


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
      this.router.navigate(['/home']);
    } else {
      console.error('No role selected');
    }
  }
}
