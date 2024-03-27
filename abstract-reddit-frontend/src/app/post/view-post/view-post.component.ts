import {Component, OnInit} from '@angular/core';
import {PostService} from "../../shared/service/post.service";
import {PostModel} from "../../shared/model/PostModel";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-view-post',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './view-post.component.html',
  styleUrl: './view-post.component.css'
})
export class ViewPostComponent implements OnInit {

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
