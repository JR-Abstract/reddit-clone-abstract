import {Component, OnInit} from '@angular/core';
import {CardModule} from "primeng/card";
import {PostModel} from "../../shared/model/PostModel";
import {ActivatedRoute} from "@angular/router";
import {PostService} from "../../shared/service/post.service";
import {ButtonModule} from "primeng/button";
import {LikeDislikeComponent} from "../../like-dislike/like-dislike.component";
import {VoteService} from "../../shared/service/vote.service";

@Component({
  selector: 'app-post-view',
  standalone: true,
  imports: [
    CardModule,
    ButtonModule,
    LikeDislikeComponent
  ],
  templateUrl: './post-view.component.html',
  styleUrl: './post-view.component.css'
})
export class PostViewComponent implements OnInit {

  postId: number;
  post: PostModel | undefined;


  constructor(private activateRoute: ActivatedRoute, private postService: PostService, private voteService: VoteService) {
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

  loadPost(postId: number): void {
    this.postService.getPost(postId).subscribe({
      next: (result) => {
        this.post = result;
      },
      error: (error) => {
        console.error('Failed to load post:', error);
      }
    });
  }

  onUpVote(postId: any): void {
    this.voteService.handleUpVotePost(postId);
    setTimeout(() => {
      this.loadPost(postId);
    }, 100);
  }

  onDownVote(postId: any): void {
    this.voteService.handleDownVotePost(postId);
    setTimeout(() => {
      this.loadPost(postId);
    }, 100);
  }
}
