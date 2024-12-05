import { CanActivateFn, Router } from '@angular/router';
import { RoleService } from './shared/services/role.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const roleService = inject(RoleService);
  const router = inject(Router);

  // Haal de rol van de gebruiker op
  const role = roleService.getRole();


  if (role) {
    // Alleen toegang tot de /create pagina als de rol 'editor' is
    if (role === 'editor') {
      return true;
    } else {
      router.navigate(['/home']);
      return false;
    }
  } else {
    router.navigate(['/login']);
    return false;
  }
};
