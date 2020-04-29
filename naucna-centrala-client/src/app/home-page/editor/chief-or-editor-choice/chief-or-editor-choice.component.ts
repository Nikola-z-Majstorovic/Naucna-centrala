import { Component, OnInit } from '@angular/core';
import {ValidationService} from '../../../services/validation.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {RepositoryService} from '../../../services/repository.service';
import {SciencePaperService} from '../../../services/science-paper.service';

@Component({
  selector: 'app-chief-or-editor-choice',
  templateUrl: './chief-or-editor-choice.component.html',
  styleUrls: ['./chief-or-editor-choice.component.css']
})
export class ChiefOrEditorChoiceComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  odluka = [];

  constructor(private sciencePaperService: SciencePaperService, private repoService: RepositoryService,
              private route: ActivatedRoute, private router: Router, private validationService: ValidationService) { }

  ngOnInit(): void {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    );
    this.repoService.getChiefEditorChoiceForm(this.taskId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach((field) => {
          if (field.type.name == 'enum') {
            this.odluka = Object.keys(field.type.values);
          }
        });
      },
      (error) => {
        alert(error.message);
      }
    );
  }
  onSubmit(value, form) {
    if(!this.validationService.validate(this.formFieldsDto.formFields, form)){
      return;
    }

    var dto = new Array();
    for (var property in value){
      dto.push({fieldId: property, fieldValue: value[property]});
    }
    this.sciencePaperService.chiefOrEditorChoice(this.taskId, dto).subscribe(
      (response) => {
        alert(response);
        this.router.navigate(['/home-page']);
      },
      (error) => {
        alert(error.message);
      }
    );
  }
}
