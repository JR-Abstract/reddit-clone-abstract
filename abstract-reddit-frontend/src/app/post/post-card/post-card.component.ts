import { Component, Input } from '@angular/core';
import { CardModule } from "primeng/card";
import { ButtonModule } from "primeng/button";
import { BadgeModule } from "primeng/badge";
import { PostModel } from "../../shared/model/PostModel";
import { Router, RouterLink } from "@angular/router";
import { LikeDislikeComponent } from "../../like-dislike/like-dislike.component";
import { PostService } from "../../shared/service/post.service";
import { VoteService } from "../../shared/service/vote.service";
import {CommentsButtonComponent} from "../../comments-button/comments-button.component";

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [
    CardModule,
    ButtonModule,
    BadgeModule,
    RouterLink,
    LikeDislikeComponent,
    CommentsButtonComponent
  ],
  templateUrl: './post-card.component.html',
  styleUrl: './post-card.component.css'
})
export class PostCardComponent {
  @Input() post: PostModel | undefined
  private http: any;

  constructor(private router: Router, private postService: PostService, private voteService: VoteService) {}

  navigateToPost(postId: number | undefined): void {
    if (postId) {
      this.router.navigateByUrl('/post/' + postId);
    }
  }

  onUpVote(postId: any): void {
    this.voteService.handleUpVotePost(postId);
    setTimeout(() => {
      this.loadPost();
    }, 100);
  }

  onDownVote(postId: any): void {
    this.voteService.handleDownVotePost(postId);
    setTimeout(() => {
      this.loadPost();
    }, 100);
  }

  private loadPost(): void {
    if (this.post && this.post.id) {
      this.postService.getPost(this.post.id).subscribe({
        next: (result) => {
          this.post = result;
        },
        error: (error) => {
          console.error('Failed to load post:', error);
        }
      });
    }
  }
  onCommentsButtonClick(event: MouseEvent): void {
    event.stopPropagation();
  }
}
