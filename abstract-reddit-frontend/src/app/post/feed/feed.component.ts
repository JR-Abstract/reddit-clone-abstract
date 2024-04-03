import { Component } from '@angular/core';
import {PostCardComponent} from "../post-card/post-card.component";
import {NgForOf} from "@angular/common";
import {PostService} from "../../shared/service/post.service";
import {PostModel} from "../../shared/model/PostModel";

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [
    PostCardComponent,
    NgForOf
  ],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent {

  posts: Array<PostModel> = [];
  constructor(private postService: PostService) {
  }

  fetchPosts() : void {
    this.postService.getAllPosts().subscribe({
      next: (result) => {
        this.posts = result;
      },
      error: (error) => {
      },
      complete: () => {
      }
    });

  }

  ngOnInit(): void {
    this.fetchPosts();
  }

}
