import {Component, OnInit} from '@angular/core';
import {CardModule} from "primeng/card";
import {PostModel} from "../../shared/model/PostModel";
import {ActivatedRoute} from "@angular/router";
import {PostService} from "../../shared/service/post.service";
import {ButtonModule} from "primeng/button";

@Component({
  selector: 'app-post-view',
  standalone: true,
  imports: [
    CardModule,
    ButtonModule
  ],
  templateUrl: './post-view.component.html',
  styleUrl: './post-view.component.css'
})
export class PostViewComponent implements OnInit {

  postId: number;
  post: PostModel | undefined;


  constructor(private activateRoute: ActivatedRoute, private postService: PostService) {
    this.postId = Number(this.activateRoute.snapshot.paramMap.get('id'))
    this.postService = postService;
  }

  ngOnInit(): void {
    this.postService.getPost(this.postId).subscribe({
      next: (result) => {
        this.post = result;
      },
      error: (error) => {
      },
      complete: () => {
      }
    });
  }

}
