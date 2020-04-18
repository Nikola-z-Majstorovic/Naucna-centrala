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

  loggedUser: any;

  constructor(private http: HttpClient, private router: Router) { }

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

  logout() {
    return this.http.post('/api/auth/logout', null).subscribe(
      success => {
        localStorage.removeItem('currentUser');
        this.router.navigate(['']);
      }, error => alert('Error while trying to logout.')
    );
  }

}
