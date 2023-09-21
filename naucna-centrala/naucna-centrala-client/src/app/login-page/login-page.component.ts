import {Component, OnInit} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
  }

  onSubmit() {
    const userDTO = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };
    this.authService.login(userDTO).subscribe(
      (success) => {
              this.router.navigate(['/home-page']);
           },
      (error) => {
        alert(error);
      });
  }

}
