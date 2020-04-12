import {NgModule} from '@angular/core';
import {HomePageComponent} from '../home-page/home-page.component';
import {LoginPageComponent} from '../login-page/login-page.component';
import {AuthorComponent } from '../core/author/author.component';
import {SciencePaperSubbmitingComponent } from '../core/author/science-paper-subbmiting/science-paper-subbmiting.component';
import {SciencePaperFormComponent } from '../core/author/science-paper-subbmiting/science-paper-form/science-paper-form.component';
import {AuthService} from '../services/auth.service';
import { GenericService } from '../services/generic/generic.service';
import {JwtUtilsService} from '../services/jwt-utils/jwt-utils.service';
import {RegisterPageComponent} from '../register-page/register-page.component';
import {RouterModule} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    HomePageComponent,
    LoginPageComponent,
    RegisterPageComponent,
    AuthorComponent,
    SciencePaperSubbmitingComponent,
    SciencePaperFormComponent],
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
    {provide: 'BASE_API_URL', useValue: 'http://localhost:8080/'}
  ]
})
export class CoreModule { }
