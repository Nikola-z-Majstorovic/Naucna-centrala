import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CoauthorService {

  constructor(private httpClient: HttpClient) { }

  save(taskId, dto){
    return this.httpClient.post('/api/task/'.concat(taskId), dto, {responseType: 'text'});
  }
}
