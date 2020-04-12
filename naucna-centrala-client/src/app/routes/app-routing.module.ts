import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginPageComponent} from '../login-page/login-page.component';
import {HomePageComponent} from '../home-page/home-page.component';
import {AuthorComponent} from '../core/author/author.component';
import {RegisterPageComponent} from '../register-page/register-page.component';


const routes: Routes = [
  { path: 'home-page', component: HomePageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'author-page', component: AuthorComponent },
  { path: '', redirectTo: 'home-page', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
