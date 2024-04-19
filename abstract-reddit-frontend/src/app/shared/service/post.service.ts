import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PostModel} from "../model/PostModel";
import {Injectable} from "@angular/core";


@Injectable({
  providedIn: 'root'
})
export class PostService {
  constructor(private http: HttpClient) {
  }

  getAllPosts(): Observable<Array<PostModel>> {
    let accessToken = localStorage.getItem("accessToken");
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + accessToken);
    return this.http.get<Array<PostModel>>('http://localhost:8080/api/v1/posts', {headers: headers});
  }

  getPost(postId: number): Observable<PostModel> {
    let accessToken = localStorage.getItem("accessToken");
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + accessToken);
    return this.http.get<PostModel>('http://localhost:8080/api/v1/posts/' + postId, {headers: headers});
  }
}
