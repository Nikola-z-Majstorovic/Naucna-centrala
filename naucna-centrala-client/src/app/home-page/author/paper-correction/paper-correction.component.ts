import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {CoauthorService} from '../../../services/coauthor.service';
import {RepositoryService} from '../../../services/repository.service';
import {SciencePaperService} from '../../../services/science-paper.service';
import {ValidationService} from '../../../services/validation.service';

@Component({
  selector: 'app-paper-correction',
  templateUrl: './paper-correction.component.html',
  styleUrls: ['./paper-correction.component.css']
})
export class PaperCorrectionComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  reviewersForm = [];
  fileUrl: string;
  fileToUpload: File;

  constructor(private coauthorService: CoauthorService,
              private repoService: RepositoryService, private validationService: ValidationService,
              private sciencePaperService: SciencePaperService,
              private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
      this.route.params.subscribe(
        (params: Params) => {
          this.taskId = params['id'];
        }
      );
      this.repoService.getPaperCorrectionForm(this.taskId).subscribe(
        (response: any) => {
          this.formFieldsDto = response;
          this.formFields = response.formFields;
          this.reviewersForm = response.reviewersForm;
        },
        (error) => {
          alert(error.message);
        }
      );
    }
  handleFileInput(file: FileList) {
    this.fileToUpload = file.item(0);
    var reader = new FileReader();
    reader.onload = (event:any) => {
      this.fileUrl = event.target.result;
    };
    reader.readAsDataURL(this.fileToUpload);
    // console.log('URL ' + this.fileUrl);
    // console.log('file ' + this.fileToUpload);
    // console.log('filename ' + this.fileToUpload.name);
  }

  onSubmit(value, form) {

    if (!this.validationService.validate(this.formFieldsDto.formFields, form)) {
      return;
    }
    let dto = new Array();
    for (var property in value) {
      if (property == 'pdf') {
        dto.push({fieldId: property, fieldValue: this.fileToUpload.name});
      }
      dto.push({fieldId: property, fieldValue: value[property]});
    }
    this.sciencePaperService.paperCorrection(this.taskId, dto).subscribe(
      (response: any) => {
        this.sciencePaperService.savePdf(response, this.fileToUpload).subscribe(
          (response) => {
            alert(response);
            this.router.navigate(['/home-page/author']);
          },
          (error) => {
            alert(error.message);
          });
      },
      (error) => { alert(error.message);
      }
    );
  }
}
