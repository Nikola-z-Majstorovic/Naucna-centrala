import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class RepositoryService {


  constructor(private httpClient: HttpClient) {
  }


  claimTask(taskId) {
    return this.httpClient.post('/api/task/claim/' + taskId, null);
  }

  getForm(taskId) {
    return this.httpClient.get('/api/task/form/' + taskId);
  }
  getPaperCorrectionForm(taskId) {
    return this.httpClient.get('/api/task/chief-or-editor-choice-and-author-correction/'.concat(taskId));
  }

  getSciencePaperForm(processInstanceId) {
    return this.httpClient.get('/api/science-paper/form/'.concat(processInstanceId));
  }

  getAddCoauthorTasks() {
    return this.httpClient.get('/api/task/coauthor');
  }

  getReviewPaperTasks() {
    return this.httpClient.get('/api/task/review-paper');
  }

  getPaperFormatForm(processId) {
    return this.httpClient.get('/api/science-paper/form/paper-format/'.concat(processId));
  }
  getPaperCorrectionTasks() {
    return this.httpClient.get('/api/task/paper-correction');
  }

  getChooseReviewerTasks() {
    return this.httpClient.get('/api/task/choose-reviewer');
  }
  getChooseOtherReviewerTasks() {
    return this.httpClient.get('/api/task/choose-other-reviewer');
  }
  getChiefEditorChoiceForm(taskId)  {
    return this.httpClient.get('/api/task/chief-or-editor-choice-and-author-correction/'.concat(taskId));
  }

  getChiefOrEditorChoiceTasks() {
    return this.httpClient.get('/api/task/chief-or-editor-choice');
  }

  getChooseReviwersForm(taskId) {
    return this.httpClient.get('/api/task/form/choose-reviewers/'.concat(taskId));
  }


  getPaperReviewTasks() {
    return this.httpClient.get('/api/task/paper-review/');
  }

  saveReview(taskId, dto) {
    return this.httpClient.post('/api/users/'.concat(taskId), dto, {responseType: 'text'});
  }

  addingTime(taskId, dto) {
    return this.httpClient.post('/api/task/addingTime/'.concat(taskId), dto, {responseType: 'text'});
  }

  getChooseTimeErrorTasks() {
    return this.httpClient.get('/api/task/choose-time-error');
  }


}
