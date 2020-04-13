import {NgModule} from '@angular/core';
import {HomePageComponent} from '../home-page/home-page.component';
import {LoginPageComponent} from '../login-page/login-page.component';
import {AuthorComponent } from '../home-page/author/author.component';
import {AuthService} from '../services/auth.service';
import { GenericService } from '../services/generic/generic.service';
import {JwtUtilsService} from '../services/jwt-utils/jwt-utils.service';
import {RegisterPageComponent} from '../register-page/register-page.component';
import {RouterModule} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import {TextSubbmitingComponent} from '../home-page/author/text-subbmiting/text-subbmiting.component';
import {SciencePaperFormComponent} from '../home-page/author/text-subbmiting/science-paper-form/science-paper-form.component';
import {AuthorTasksComponent} from '../home-page/author/author-tasks/author-tasks.component';
import { ChooseMagazineComponent } from '../home-page/author/text-subbmiting/choose-magazine/choose-magazine.component';
import {JwtInterceptor} from '../_helper/jwt.interceptor';

@NgModule({
  declarations: [
    HomePageComponent,
    LoginPageComponent,
    RegisterPageComponent,
    AuthorComponent,
    TextSubbmitingComponent,
    SciencePaperFormComponent,
    AuthorTasksComponent,
    ChooseMagazineComponent],
  imports: [
    CommonModule,
    ToastrModule.forRoot({preventDuplicates: true}), // ToastrModule added,
    RouterModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    AuthService,
    GenericService,
    JwtUtilsService,
    // {provide: 'BASE_API_URL', useValue: 'http://localhost:8080/'}
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ]
})
export class CoreModule { }
