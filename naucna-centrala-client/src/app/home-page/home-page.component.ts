import { Component, OnInit } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {UserService} from '../services/UserService';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  constructor(private authService: AuthService, private userService: UserService) {
  }

  isAdmin = false;
  isReviewer = false;
  isEditor = false;
  isAuthor = false;
  isLoggedIn = false;


  ngOnInit() {
    const user = this.authService.getLoggedUser();
    if (user != null) {
      this.isLoggedIn = true;
    }

    this.userService.getUser().subscribe(
      (user: any) => {
        console.log(user);
        if(user == null) {
          this.isLoggedIn = false;
          console.log(this.isLoggedIn);
          console.log('ROLA: NEMA');
        } else if (user.role === 'ADMIN') {
          this.isAdmin = true;
          console.log('ROLA: ADMIN');
        } else if (user.role === 'REVIEWER') {
          this.isReviewer = true;
          console.log('ROLA: RECENZENT');
        } else if (user.role === 'EDITOR') {
          this.isEditor = true;
          console.log('ROLA: UREDNIK');
        } else if (user.role == 'AUTHOR') {
          this.isAuthor = true;
          console.log('ROLA: AUTOR');
        }
      }
    );

  }

  logOut() {
    this.authService.logout();
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.isEditor = false;
    this.isReviewer = false;
    this.isAuthor = false;
  }

}
