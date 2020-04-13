import {Inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {GenericService} from './generic/generic.service';
import {catchError, map} from 'rxjs/operators';
import {JwtUtilsService} from './jwt-utils/jwt-utils.service';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private router: Router) { }

  loggedUser: any;
  private relativeUrl;
  private currentUserKey = 'currentUser';


  getLoggedUser() {
    return localStorage.getItem('currentUser');
  }

  login(user) {
    return this.http.post('/api/auth/login', user)
      .pipe(
        map(user => {
          this.loggedUser = user;
          if (user) {
            localStorage.setItem('currentUser', JSON.stringify(user));
          }
          return user;
        })
      );
  }
  // getToken(): string {
  //   const currentUser = JSON.parse(localStorage.getItem(this.currentUserKey));
  //   const token = currentUser && currentUser.token;
  //   return token ? token : '';
  // }

  logout() {
    return this.http.post('/api/auth/logout', null).subscribe(
      success => {
        localStorage.removeItem('currentUser');
        this.router.navigate(['']);
      }, error => alert('Error while trying to logout.')
    );
  }

  // isLoggedIn(): boolean {
  //   if (this.getToken() !== '') {
  //     return true;
  //   }
  //   return false;
  // }
  //
  // getCurrentUser() {
  //   if (localStorage.currentUser) {
  //     return JSON.parse(localStorage.currentUser);
  //   } else {
  //     return undefined;
  //   }
  // }
}
