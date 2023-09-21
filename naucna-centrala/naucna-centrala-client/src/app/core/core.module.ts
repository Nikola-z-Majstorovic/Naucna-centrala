import {NgModule} from '@angular/core';
import {HomePageComponent} from '../home-page/home-page.component';
import {LoginPageComponent} from '../login-page/login-page.component';
import {AuthorComponent } from '../home-page/author/author.component';
import {AuthService} from '../services/auth.service';
import { GenericService } from '../services/generic/generic.service';
import {JwtUtilsService} from '../services/jwt-utils/jwt-utils.service';
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
import { MembershipPaymentComponent } from '../home-page/author/text-subbmiting/membership-payment/membership-payment.component';
import {CoauthorFormComponent} from '../home-page/author/coauthor-form/coauthor-form.component';
import { EditorComponent } from '../home-page/editor/editor.component';
import { EditorPapersComponent } from '../home-page/editor/editor-papers/editor-papers.component';
import { ReviewPaperComponent } from '../home-page/editor/review-paper/review-paper.component';
import { ChooseReviwersComponent } from '../home-page/editor/choose-reviwers/choose-reviwers.component';
import { PaperFormatComponent } from '../home-page/editor/paper-format/paper-format.component';
import { PaperReviewComponent } from '../home-page/reviewer/paper-review/paper-review.component';
import { ReviewerComponent } from '../home-page/reviewer/reviewer.component';
import { PaperSubbmitComponent } from '../home-page/reviewer/paper-subbmit/paper-subbmit.component';
import { ChiefOrEditorChoiceComponent } from '../home-page/editor/chief-or-editor-choice/chief-or-editor-choice.component';
import { PaperCorrectionComponent } from '../home-page/author/paper-correction/paper-correction.component';
import { ChoosingErrorTimeComponent } from '../home-page/editor/choosing-error-time/choosing-error-time.component';
import { ChooseOtherReviwersComponent } from '../home-page/editor/choose-other-reviwers/choose-other-reviwers.component';
@NgModule({
  declarations: [
    HomePageComponent,
    LoginPageComponent,
    AuthorComponent,
    TextSubbmitingComponent,
    SciencePaperFormComponent,
    AuthorTasksComponent,
    ChooseMagazineComponent,
    MembershipPaymentComponent,
    CoauthorFormComponent,
    EditorComponent,
    EditorPapersComponent,
    ReviewPaperComponent,
    ChooseReviwersComponent,
    PaperFormatComponent,
    ReviewerComponent,
    PaperReviewComponent,
    PaperSubbmitComponent,
    ChiefOrEditorChoiceComponent,
    PaperCorrectionComponent,
    ChoosingErrorTimeComponent,
    ChooseOtherReviwersComponent],
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
