import { Routes } from '@angular/router';
import { CreatePostComponent } from './core/posts/create-post/create-post.component';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/login/login.component';
import { authGuard } from './auth.guard';
import { EditPostComponent } from './core/posts/edit-post/edit-post.component';
import { PostDashboardComponent } from './core/posts/post-dashboard/post-dashboard.component';
import { ReviewPostComponent } from './core/reviews/review-post/review-post.component';
import { ReviewListComponent } from './core/reviews/review-list/review-list.component';
import { CommentPostComponent } from './core/comments/comment-post/comment-post.component';
import { PostDetailComponent } from './core/posts/post-detail/post-detail.component';
import { CommentEditComponent } from './core/comments/comment-edit/comment-edit.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'create-post', component: CreatePostComponent, canActivate: [authGuard] },
  { path: 'dashboard', component: PostDashboardComponent, canActivate: [authGuard] },
  { path: 'post-edit/:id', component: EditPostComponent, canActivate: [authGuard] },
  { path: 'review-post/:id', component: ReviewPostComponent, canActivate: [authGuard] },
  { path: 'review', component: ReviewListComponent, canActivate: [authGuard] },
  { path: 'comment-post/:id', component: CommentPostComponent},
  { path: 'read-post/:id', component: PostDetailComponent},
  { path: 'comment-edit/:id', component: CommentEditComponent}
];

