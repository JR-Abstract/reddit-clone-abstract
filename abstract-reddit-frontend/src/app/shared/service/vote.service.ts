import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {VoteModel} from '../model/VoteModel';
import {catchError} from 'rxjs/operators';
import {throwError} from 'rxjs';
import {PostService} from './post.service';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  constructor(private http: HttpClient, private postService: PostService) {
  }

  handleUpVotePost(entityId: any): void {
    const voteDto: VoteModel = {
      voteType: 'UPVOTE',
      userId: 1, // Пізніше треба буде додати ідентифікатор користувача
      voteEntityId: entityId,
      voteEntityType: 'POST'
    };

    this.saveVote(voteDto);
  }

  handleDownVotePost(entityId: any): void {
    const voteDto: VoteModel = {
      voteType: 'DOWNVOTE',
      userId: 1, // Пізніше треба буде додати ідентифікатор користувача
      voteEntityId: entityId,
      voteEntityType: 'POST'
    };

    this.saveVote(voteDto);
  }

  private saveVote(voteDto: VoteModel): void {
    this.http.post<any>('http://localhost:8080/api/v1/votes', voteDto).pipe(
      catchError(error => {
        console.error('Failed to save vote:', error);
        return throwError(error);
      })
    ).subscribe(() => {
      console.log('Vote saved successfully');
    });
  }
}
