import { Routes } from '@angular/router';
import { CreatePostComponent } from './core/posts/create-post/create-post.component';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/login/login.component';
import { authGuard } from './auth.guard';
import { EditPostComponent } from './core/posts/edit-post/edit-post.component';
import { PostDashboardComponent } from './core/posts/post-dashboard/post-dashboard.component';
import { ReviewPostComponent } from './core/reviews/review-post/review-post.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'create', component: CreatePostComponent, canActivate: [authGuard] },
  { path: 'dashboard', component: PostDashboardComponent, canActivate: [authGuard] },
  { path: 'edit/:id', component: EditPostComponent },
  { path: 'review-post/:id', component: ReviewPostComponent }
  // { path: 'reject-post/:id', component: RejectPostComponent }
];

// export const routes: Routes = [
//   { path: 'home', component: HomeComponent},
//   { path: '', redirectTo: 'home', pathMatch: 'full' },
//   { path: 'create', component: CreatePostComponent },
// ];
