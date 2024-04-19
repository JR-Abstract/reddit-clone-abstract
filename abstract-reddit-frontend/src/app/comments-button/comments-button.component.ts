import { Component, Input, OnInit } from '@angular/core';
import { CommentService } from "../shared/service/comment.service";

@Component({
  selector: 'app-comments-button',
  templateUrl: './comments-button.component.html',
  standalone: true,
  styleUrls: ['./comments-button.component.css']
})
export class CommentsButtonComponent implements OnInit {
  @Input() postId: number | undefined;
  count: number = 0;

  constructor(private commentService: CommentService) {}

  ngOnInit(): void {
    if (this.postId) {
      this.loadCommentCount();
    } else {
      console.error('Post ID is not defined');
    }
  }
  loadCommentCount(): void {
    this.commentService.getCountByPostId(this.postId).subscribe({
      next: (count) => {
        this.count = count;
      },
      error: (error) => {
        console.error('Failed to load comment count:', error);
      }
    });
  }
}
