import { Component } from '@angular/core';
import { PostListComponent } from "../posts/post-list/post-list.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [PostListComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
