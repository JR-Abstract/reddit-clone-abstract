import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  constructor(private http: HttpClient) {}

  getCountByPostId(postId: number | undefined): Observable<number> {
    return this.http.get<number>(`http://localhost:8080/api/v1/comments/count/${postId}`).pipe(
    );
  }
}
