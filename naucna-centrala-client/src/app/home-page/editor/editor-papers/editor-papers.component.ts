import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../../../services/repository.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-editor-papers',
  templateUrl: './editor-papers.component.html',
  styleUrls: ['./editor-papers.component.css']
})
export class EditorPapersComponent implements OnInit {

  paperReviewTasks = [];
  chooseReviewerTasks = [];
  chiefOrEditorChoiceTasks = [];
  chooseTimeErrorTasks = [];
  chooseOtherReviewerTasks = [];

  constructor(private repoService: RepositoryService, private router: Router) { }

    ngOnInit(): void {
      this.repoService.getReviewPaperTasks().subscribe(
        (response: any) => {
          this.paperReviewTasks = response;
        },
        (error) => { alert(error.message);
        }
      );
      this.repoService.getChooseReviewerTasks().subscribe(
        (response: any) => {
          this.chooseReviewerTasks = response;
        },
        (error) => { alert(error.message);
        }
      );
      this.repoService.getChooseOtherReviewerTasks().subscribe(
        (response: any) => {
          this.chooseOtherReviewerTasks = response;
        },
        (error) => { alert(error.message);
        }
      );
      this.repoService.getChiefOrEditorChoiceTasks().subscribe(
        (response: any) => {
          this.chiefOrEditorChoiceTasks = response;
        },
        (error) => { alert(error.message);
        }
      );
      this.repoService.getChooseTimeErrorTasks().subscribe(
        (response: any) => {
          this.chooseTimeErrorTasks = response;
        },
        (error) => { alert(error.message);
        }
      );

  }

  claimPaperReviweTask(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/editor/review-paper/' + taskId]);
      }
    );
  }
  claimChooseReviewerTask(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/editor/choose-reviwers/'.concat(taskId)]);
      }
    );
  }
  claimChooseOtherReviewerTask(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/editor/choose-other-reviwers/'.concat(taskId)]);
      }
    );
  }
  claimChiefOrEditorChoiceTask(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/editor/chief-or-editor-choice/'.concat(taskId)]);
      }
    );
  }

  claimChooseTimeErrorTasks(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/editor/choosing-error-time/'.concat(taskId)]);
      }
    );
  }

}
