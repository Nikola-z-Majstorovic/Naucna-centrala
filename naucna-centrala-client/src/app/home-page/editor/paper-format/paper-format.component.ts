import { Component, OnInit } from '@angular/core';
import {ValidationService} from '../../../services/validation.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {SciencePaperService} from '../../../services/science-paper.service';
import {RepositoryService} from '../../../services/repository.service';

@Component({
  selector: 'app-paper-format',
  templateUrl: './paper-format.component.html',
  styleUrls: ['./paper-format.component.css']
})
export class PaperFormatComponent implements OnInit {

  processId: any;
  formFieldsDto = null;
  formFields = [];
  formatiranost = [];

  constructor(private repoService: RepositoryService, private sciencePaperService: SciencePaperService,
              private route: ActivatedRoute, private router: Router, private validationService: ValidationService) {}

  ngOnInit(): void {
    this.route.params.subscribe(
      (params: Params) => {
        this.processId = params['processId'];
      });
    this.repoService.getPaperFormatForm(this.processId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach((field) => {
          if (field.type.name == 'enum') {
            this.formatiranost = Object.keys(field.type.values);
          }
        });
      }
    );
  }
  onSubmit(value, form) {

    if (!this.validationService.validate(this.formFieldsDto.formFields, form)) {
      return;
    }

    var dto = new Array();
    for (var property in value) {
      dto.push({fieldId: property, fieldValue: value[property]});
    }

    this.sciencePaperService.paperFormat(this.formFieldsDto.taskId, dto).subscribe(
      (response) => {
        alert(response);
        this.router.navigate(['/home-page']);
      },
      (error) => {
        alert(error.message);
      }
    );

  }

  onDownload() {
    this.sciencePaperService.download(this.processId).subscribe(
    (response: any) => {
      alert('Skinut pdf!');
      var blob = new Blob([response], {type: 'application/pdf'});
      var url = window.URL.createObjectURL(blob);
      console.log(url);
      window.open(url, '_blank');
    },
    (error) => {
      alert(error.message);
    }
  );
  }

}
