import {Injectable} from '@angular/core';
import {LoginRequestPayload} from "../login/payload/LoginRequestPayload";
import {HttpClient} from "@angular/common/http";
import {LoginResponsePayload} from "../login/payload/LoginResponsePayload";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) {
  }

  login(loginRequestPayload: LoginRequestPayload) {
    return this.httpClient.post<LoginResponsePayload>('http://localhost:8080/api/v1/auth/login', loginRequestPayload)
      .subscribe(response => {
        localStorage.setItem('accessToken', response.access_token);
        localStorage.setItem('refreshToken', response.refresh_token);

        console.log(response);
      })
  }
}
