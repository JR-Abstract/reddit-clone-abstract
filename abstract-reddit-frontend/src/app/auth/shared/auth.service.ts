import {Injectable} from '@angular/core';
import {LoginRequestPayload} from "../login/payload/LoginRequestPayload";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) {
  }

  login(loginRequestPayload: LoginRequestPayload) {


    this.httpClient.post('http://localhost:8080/api/v1/auth/login', loginRequestPayload).subscribe(data => {
      console.log(data);
    })


  }
}
