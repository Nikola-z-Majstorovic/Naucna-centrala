import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MockService {

  constructor(private httpClient: HttpClient) { }

  startPaymentProcess(processId) {
    return this.httpClient.get('/api/science-paper/payment/'.concat(processId));
  }
  payment(taskId, dto, processInstanceId) {
    return this.httpClient.post('/api/science-paper/submit/'.concat(taskId) +  '/'.concat(processInstanceId), dto, {responseType: 'text'});
  }
}
