import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginPageComponent} from '../login-page/login-page.component';
import {HomePageComponent} from '../home-page/home-page.component';
import { AuthorComponent } from '../home-page/author/author.component';
import {TextSubbmitingComponent} from '../home-page/author/text-subbmiting/text-subbmiting.component';
import {SciencePaperFormComponent} from '../home-page/author/text-subbmiting/science-paper-form/science-paper-form.component';
import {AuthorTasksComponent} from '../home-page/author/author-tasks/author-tasks.component';
import {ChooseMagazineComponent} from '../home-page/author/text-subbmiting/choose-magazine/choose-magazine.component';
import {MembershipPaymentComponent} from '../home-page/author/text-subbmiting/membership-payment/membership-payment.component';
import {CoauthorFormComponent} from '../home-page/author/coauthor-form/coauthor-form.component';
import {EditorPapersComponent} from '../home-page/editor/editor-papers/editor-papers.component';
import {ReviewPaperComponent} from '../home-page/editor/review-paper/review-paper.component';
import {EditorComponent} from '../home-page/editor/editor.component';
import {PaperFormatComponent} from '../home-page/editor/paper-format/paper-format.component';
import {ChooseReviwersComponent} from '../home-page/editor/choose-reviwers/choose-reviwers.component';

const routes: Routes = [
  { path: '', redirectTo: 'home-page', pathMatch: 'full'},
  { path: 'login', component: LoginPageComponent },
  { path: 'home-page', component: HomePageComponent , children: [
  { path: 'author', component: AuthorComponent, children: [
    { path: '', component: AuthorTasksComponent},
    { path: 'text-subbmiting', component: TextSubbmitingComponent, children: [
      {path: '', component: ChooseMagazineComponent},
      {path: 'membership-payment/:processId', component: MembershipPaymentComponent},
      {path: 'science-paper-form/:processId', component: SciencePaperFormComponent}]
    },
    { path: 'coauthor/:id', component: CoauthorFormComponent },
  ]},
  { path: 'editor', component: EditorComponent, children: [
    { path: 'papers', component: EditorPapersComponent },
    { path: 'paper-format/:processId', component: PaperFormatComponent },
    { path: 'review-paper/:id', component: ReviewPaperComponent },
    { path: 'choose-reviwers/:id', component: ChooseReviwersComponent },
   ]}
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
