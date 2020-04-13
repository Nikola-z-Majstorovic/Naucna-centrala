import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SciencePaperService {

  constructor(private httpClient: HttpClient) { }

  startProcess() {
    return this.httpClient.get('/api/science-paper/form');
  }
  selectMagazine(taskId, dto) {
    return this.httpClient.post('/api/science-paper/select-magazine/'.concat(taskId), dto);
  }
  save(taskId, dto) {
    return this.httpClient.post('/api/science-paper/'.concat(taskId), dto, {responseType: 'text'});
  }

  savePdf(sciencePaperId, fileToUpload) {
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload);
    return this.httpClient.put('/api/science-paper/'.concat(sciencePaperId), formData, {responseType: 'text'});
  }
}
