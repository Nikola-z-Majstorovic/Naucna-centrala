import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../../../services/repository.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-paper-review',
  templateUrl: './paper-review.component.html',
  styleUrls: ['./paper-review.component.css']
})
export class PaperReviewComponent implements OnInit {

  reviewPaperTasks = [];

  constructor(private repoService: RepositoryService, private router: Router) { }

  ngOnInit(): void {
    this.repoService.getPaperReviewTasks().subscribe(
      (response: any) => {
        this.reviewPaperTasks = response;
      },
      (error) => { alert(error.message);
      }
    );

  }

  claimReviewPaperTask(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/reviewer/paper-subbmit/' + taskId]);
      }
    );
  }

}
