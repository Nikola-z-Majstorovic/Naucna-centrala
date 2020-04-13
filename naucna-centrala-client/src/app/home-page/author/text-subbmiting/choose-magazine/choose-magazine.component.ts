import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { SciencePaperService } from 'src/app/services/science-paper.service';
import { ValidationService } from 'src/app/services/validation.service';
import { NgForm, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-choose-magazine',
  templateUrl: './choose-magazine.component.html',
  styleUrls: ['./choose-magazine.component.css']
})
export class ChooseMagazineComponent implements OnInit {

  @ViewChild('f', { static: true })
  form: NgForm;

  casopisi: any = [];
  formFieldsDto: any;
  formFields: any;

  constructor(private sciencePaperService: SciencePaperService, private validationService: ValidationService, private router: Router) {
    this.sciencePaperService.startProcess().subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach( (field) => {
          if ( field.type.name == 'enum') {
            this.casopisi = Object.keys(field.type.values);
          }
        });
      },
      (error) => { alert(error.message); }
    );
  }

  ngOnInit() {
  }

  // onSubmit(value, form) {
  //
  //   if (!this.validationService.validate(this.formFieldsDto.formFields, form)) {
  //     return;
  //   }
  //
  //   let dto = new Array();
  //
  //   for (var property in value) {
  //     dto.push({fieldId: property, fieldValue: value[property]});
  //   }
  //   this.sciencePaperService.selectMagazine(this.formFieldsDto.taskId, dto).subscribe(
  //
  //     (response: any) => {
  //       if (response.openAccess == false && response.membership == false) {
  //         this.router.navigate(['/homepage/author/text-subbmiting/science-paper-form/'.concat(this.formFieldsDto.processInstanceId)]);
  //       }
  //       if (response.openAccess == true && response.membership == false) {
  //         this.router.navigate(['/homepage/author/text-subbmiting/membership-payment/'.concat(this.formFieldsDto.processInstanceId)]);
  //       }
  //       if (response.openAccess == true && response.membership == true) {
  //         this.router.navigate(['/homepage/author/text-subbmiting/science-paper-form/'.concat(this.formFieldsDto.processInstanceId)]);
  //       }
  //
  //     },
  //     (error) => {
  //       alert(error.message);
  //     });
  // }


}
