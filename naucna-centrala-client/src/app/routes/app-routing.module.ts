import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginPageComponent} from '../login-page/login-page.component';
import {HomePageComponent} from '../home-page/home-page.component';
import { AuthorComponent } from '../home-page/author/author.component';
import {RegisterPageComponent} from '../register-page/register-page.component';
import {TextSubbmitingComponent} from '../home-page/author/text-subbmiting/text-subbmiting.component';
import {SciencePaperFormComponent} from '../home-page/author/text-subbmiting/science-paper-form/science-paper-form.component';
import {AuthorTasksComponent} from '../home-page/author/author-tasks/author-tasks.component';
import {ChooseMagazineComponent} from '../home-page/author/text-subbmiting/choose-magazine/choose-magazine.component';
import {MembershipPaymentComponent} from '../home-page/author/text-subbmiting/membership-payment/membership-payment.component'
import {CoauthorFormComponent} from '../home-page/author/coauthor-form/coauthor-form.component';

const routes: Routes = [
  { path: '', redirectTo: 'home-page', pathMatch: 'full'},
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'home-page', component: HomePageComponent , children: [
  { path: 'author', component: AuthorComponent, children: [
    { path: '', component: AuthorTasksComponent},
    { path: 'text-subbmiting', component: TextSubbmitingComponent, children: [
      {path: '', component: ChooseMagazineComponent},
      {path: 'membership-payment/:processId', component: MembershipPaymentComponent},
      {path: 'science-paper-form/:processId', component: SciencePaperFormComponent}]
    },
    { path: 'coauthor/:id', component: CoauthorFormComponent },
  ]}

  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
