import { Component } from '@angular/core';
import {HeaderComponent} from "../../header/header.component";
import {FeedComponent} from "../../post/feed/feed.component";

@Component({
  selector: 'app-home-view',
  standalone: true,
  imports: [
    HeaderComponent,
    FeedComponent
  ],
  templateUrl: './home-view.component.html',
  styleUrl: './home-view.component.css'
})
export class HomeViewComponent {

    constructor() {
      console.log("HomeViewComponent")
    }

    ngOnInit(): void {
    }

}
