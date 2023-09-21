import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {MockService} from '../../../../services/mock.service';
import {resolveFileWithPostfixes} from '@angular/compiler-cli/ngcc/src/utils';

@Component({
  selector: 'app-membership-payment',
  templateUrl: './membership-payment.component.html',
  styleUrls: ['./membership-payment.component.css']
})
export class MembershipPaymentComponent implements OnInit {

  casopisi: any = [];
  formFieldsDto: any;
  formFields: any;
  processId: any;

  constructor(private mockService: MockService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        this.processId = params['processId'];
      }
    );
    this.mockService.startPaymentProcess(this.processId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach( (field) => {
          if ( field.type.name == 'enum') {
            this.casopisi = Object.keys(field.type.values);
          }
        });
      },
      (error) => { alert(error.message);
      }
    );
  }

  onSubmit(value, form) {

    let dto = new Array();

    for (var property in value) {
      dto.push({fieldId: property, fieldValue: value[property]});
    }
    this.mockService.payment(this.formFieldsDto.taskId, dto, this.formFieldsDto.processInstanceId).subscribe(
      (response: any) => {
        if (response) {
          this.router.navigate(['/home-page/author/text-subbmiting/science-paper-form/'.concat(this.processId)]);
        } else {
          alert('Nemate dovoljno sredstva na racunu!');
          this.router.navigate(['/']);
          }
      },
      (error) => {
        alert(error.message);
      }

    );
  }
}
