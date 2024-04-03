import {Component, Input} from '@angular/core';
import {CardModule} from "primeng/card";
import {PostModel} from "../../shared/model/PostModel";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-post-view',
  standalone: true,
  imports: [
    CardModule
  ],
  templateUrl: './post-view.component.html',
  styleUrl: './post-view.component.css'
})
export class PostViewComponent {
  postId: number;
  post: PostModel | undefined;


  constructor(private activateRoute: ActivatedRoute) {
    this.postId = Number( this.activateRoute.snapshot.paramMap.get('id'))
    console.log(this.activateRoute.snapshot)
  }


}
