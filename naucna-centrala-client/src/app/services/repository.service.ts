import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  // categories = [];
  // languages = [];
  // books = [];

  constructor(private httpClient: HttpClient) {
  }

  // startProcess(){
  //   return this.httpClient.get('/api/users/form');
  // }
  //
  // getTasksReviewer(){
  //   return this.httpClient.get('/api/admin/get/tasks/reviewer');
  // }
  //
  claimTask(taskId) {
    return this.httpClient.post('/api/task/claim/' + taskId, null);
  }

  getForm(taskId) {
    return this.httpClient.get('/api/task/form/' + taskId);
  }
  //
  // getCheckMagazineDataForm(taskId){
  //   return this.httpClient.get('/api/repository/form/check-magazine-data/' + taskId);
  // }
  //
  // getMagazineCorrectionForm(taskId){
  //   return this.httpClient.get('/api/repository/form/magazine-correction/' + taskId);
  // }
  //
  // getEditorialBoardForm(processInstanceId) {
  //   return this.httpClient.get('/api/repository/form/editorial-board/' + processInstanceId);
  // }

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

  // getChooseReviwersForm(taskId) {
  //   return this.httpClient.get('/api/repository/form/choose-reviewers/'.concat(taskId));
  // }
  //
  // getChiefEditorReviewingTasks() {
  //   return this.httpClient.get('/api/repository/tasks/chief-editor-reviewing');
  // }
  //
  getChiefEditorChoiceForm(taskId)  {
    return this.httpClient.get('/api/task/chief-or-editor-choice/'.concat(taskId));
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

  // getReviewForm(taskId) {
  //   return this.httpClient.get('/api/users/form/'.concat(taskId));
  // }
  getPaperBigCorrectionTasks() {
    return this.httpClient.get('/api/task/paper-big-correction');
  }
  getPaperSmallCorrectionTasks() {
    return this.httpClient.get('/api/task/paper-small-correction');
  }
  addingTime(taskId, dto) {
    return this.httpClient.post('/api/task/addingTime/'.concat(taskId), dto, {responseType: 'text'});
  }

  getChooseTimeErrorTasks() {
    return this.httpClient.get('/api/task/choose-time-error');
  }


}
