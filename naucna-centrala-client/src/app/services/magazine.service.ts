import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  constructor(private httpClient: HttpClient) { }

  getAllByChiefEditor() {
    return this.httpClient.get('/api/magazines/get-by-editor');
  }
  getMagazineCorrectionTasks() {
    return this.httpClient.get('/api/magazines/magazine-correction');
  }
}
