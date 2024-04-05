import {Component, Input} from '@angular/core';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {BadgeModule} from "primeng/badge";
import {PostModel} from "../../shared/model/PostModel";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [
    CardModule,
    ButtonModule,
    BadgeModule,
    RouterLink
  ],
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.css'
})
export class PostCardComponent {
  @Input() post: PostModel | undefined


  constructor(private router: Router) {}

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }
}
