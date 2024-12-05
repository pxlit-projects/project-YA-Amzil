import { Routes } from '@angular/router';
import { CreatePostComponent } from './core/posts/create-post/create-post.component';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/login/login.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'create', component: CreatePostComponent, canActivate: [authGuard] },
];

// export const routes: Routes = [
//   { path: 'home', component: HomeComponent},
//   { path: '', redirectTo: 'home', pathMatch: 'full' },
//   { path: 'create', component: CreatePostComponent },
// ];
