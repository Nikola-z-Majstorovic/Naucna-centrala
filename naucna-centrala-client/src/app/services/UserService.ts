import {Inject, Injectable} from '@angular/core';

import { HttpClient} from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  // registerUser(user, taskId) {
  //   return this.httpClient.post("/api/users/register/" + taskId, user);
  // }

  getUser() {
    return this.httpClient.get('api/users/get-user');
  }
}
