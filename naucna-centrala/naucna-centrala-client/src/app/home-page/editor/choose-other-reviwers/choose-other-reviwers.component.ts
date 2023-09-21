import { Component, OnInit } from '@angular/core';
import {RepositoryService} from '../../../services/repository.service';
import {ValidationService} from '../../../services/validation.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {SciencePaperService} from '../../../services/science-paper.service';

@Component({
  selector: 'app-choose-other-reviwers',
  templateUrl: './choose-other-reviwers.component.html',
  styleUrls: ['./choose-other-reviwers.component.css']
})
export class ChooseOtherReviwersComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  recenzenti = [];

  constructor(private sciencePaperService: SciencePaperService, private repoService: RepositoryService,
              private route: ActivatedRoute, private router: Router, private validationService: ValidationService) {}

  ngOnInit(): void {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      });
    this.repoService.getChooseReviwersForm(this.taskId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        console.log(response);
        this.formFields.forEach((field) => {
          if (field.type.name == 'enum') {
            this.recenzenti = Object.keys(field.type.values);
          }
        });
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
  console.log(value);
  var dto = new Array();
  for (var property in value) {
    // if (property === 'recenzent') {
    //   var recenzenti = value[property];
    //   for (let i = 0; i < recenzenti.length; i++) {
    //     dto.push({fieldId: property, fieldValue: recenzenti[i]});
    //   }
    // } else {
      dto.push({fieldId: property, fieldValue: value[property]});
    // }
  }
  this.sciencePaperService.chooseOtherReviewers(this.taskId, dto).subscribe(
    (response: any) => {
      alert(response);
      this.router.navigate(['/home-page/editor']);
    },
    (error) => { alert(error.message);
    }
  );
  }

}
