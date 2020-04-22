import { Component, OnInit } from '@angular/core';
import {SciencePaperService} from '../../../services/science-paper.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {ValidationService} from '../../../services/validation.service';
import {CoauthorService} from '../../../services/coauthor.service';
import {RepositoryService} from '../../../services/repository.service';

@Component({
  selector: 'app-coauthor-form',
  templateUrl: './coauthor-form.component.html',
  styleUrls: ['./coauthor-form.component.css']
})
export class CoauthorFormComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];

  constructor(private coauthorService: CoauthorService, private repoService: RepositoryService, private sciencePaperService: SciencePaperService,
              private route: ActivatedRoute, private router: Router, private validationService: ValidationService) {

  }

  ngOnInit()  {
      this.route.params.subscribe(
        (params: Params) => {
          this.taskId = params['id'];
        }
      );
      this.repoService.getForm(this.taskId).subscribe(
        (response: any) => {
          this.formFieldsDto = response;
          this.formFields = response.formFields;

        },
        (error) => {
          alert(error.message);
        }
      );
    }

  onSubmit(value, form) {

    if (!this.validationService.validate(this.formFieldsDto.formFields, form)) {
      return;
    }

    let dto = new Array();
    for (var property in value) {
      dto.push({fieldId: property, fieldValue: value[property]});
    }

    this.coauthorService.save(this.taskId, dto).subscribe(
      (success) => {
        alert(success);
        this.router.navigate(['/home-page/author']);
      },
      (error) => {
        alert(error);
      }
    );
  }

}
