import { Routes } from '@angular/router';
import { CreatePostComponent } from './core/posts/create-post/create-post.component';
import { HomeComponent } from './core/home/home.component';

export const routes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'create', component: CreatePostComponent },
];
