import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-author-tasks',
  templateUrl: './author-tasks.component.html',
  styleUrls: ['./author-tasks.component.css']
})
export class AuthorTasksComponent implements OnInit {

  tasksCoauthor: any = [];
  tasksPaperCorrection: any = [];


  constructor(private repoService: RepositoryService, private router: Router) {
  }


  ngOnInit() {
    this.repoService.getAddCoauthorTasks().subscribe(
      (response: any) => {
        this.tasksCoauthor = response;
      },
      (error) => {
        alert(error.message);
      }
    );

    this.repoService.getPaperCorrectionTasks().subscribe(
      (response: any) => {
        this.tasksPaperCorrection = response;
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  claimTaskCoauthor(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/author/coauthor/'.concat(taskId)]);
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  claimTaskPaperCorrection(taskId) {
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/home-page/author/paper-correction/'.concat(taskId)]);
      },
      (error) => {
        alert(error.message);
      }
    );
  }
}
