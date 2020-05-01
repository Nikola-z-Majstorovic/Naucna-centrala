import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../../../services/repository.service';
import {SciencePaperService} from '../../../services/science-paper.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {ValidationService} from '../../../services/validation.service';

@Component({
  selector: 'app-choosing-error-time',
  templateUrl: './choosing-error-time.component.html',
  styleUrls: ['./choosing-error-time.component.css']
})
export class ChoosingErrorTimeComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  TestDateString = new Date();

  constructor(private repoService: RepositoryService, private route: ActivatedRoute,
              private router: Router, private validationService: ValidationService) {
  }

  ngOnInit(): void {
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
    this.repoService.addingTime(this.taskId, dto).subscribe(
      (response) => {
        this.router.navigate(['/home-page/editor']);
      },
      (error) => {

        }
    );
  }
}
