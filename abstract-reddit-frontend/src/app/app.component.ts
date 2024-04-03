import { Component } from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {HeaderComponent} from "./header/header.component";
import {LoginComponent} from "./auth/login/login.component";
import { ButtonModule } from 'primeng/button';
import {PostCardComponent} from "./post/post-card/post-card.component";
import {FeedComponent} from "./post/feed/feed.component";
import { RouterModule } from '@angular/router';
import {HomeViewComponent} from "./view/home-view/home-view.component";


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, LoginComponent, ButtonModule, PostCardComponent, FeedComponent, RouterModule,
    RouterOutlet, RouterLink, RouterLinkActive, HomeViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'abstract-reddit-frontend';
}
