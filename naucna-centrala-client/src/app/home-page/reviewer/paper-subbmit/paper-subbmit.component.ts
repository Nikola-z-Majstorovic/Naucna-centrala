import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {RepositoryService} from '../../../services/repository.service';
import {ValidationService} from '../../../services/validation.service';

@Component({
  selector: 'app-paper-subbmit',
  templateUrl: './paper-subbmit.component.html',
  styleUrls: ['./paper-subbmit.component.css']
})
export class PaperSubbmitComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  preporukaPrihvacanja = [];

  constructor(private route: ActivatedRoute, private repoService: RepositoryService,
              private router: Router, private validationService: ValidationService) { }

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
        this.formFields.forEach((field) => {
            if (field.type.name == 'enum') {
              this.preporukaPrihvacanja = Object.keys(field.type.values);
            }

      },
      (error) => {
        alert(error.message);
      }
    );
    });
  }

  onSubmit(value, form) {

  }
}
