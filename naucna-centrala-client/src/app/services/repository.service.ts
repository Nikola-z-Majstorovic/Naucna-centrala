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
  // claimTask(taskId){
  //   return this.httpClient.post('/api/repository/tasks/claim/' + taskId, null);
  // }
  //
  // getForm(taskId){
  //   return this.httpClient.get('/api/repository/form/' + taskId);
  // }
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
    return this.httpClient.get('/api/task/form/science-paper/'.concat(processInstanceId));
  }

  // getAddCoauthorTasks() {
  //   return this.httpClient.get('/api/repository/tasks/coauthor');
  // }
  //
  // getReviewPaperTasks() {
  //   return this.httpClient.get('/api/repository/tasks/review-paper');
  // }
  //
  // getPaperFormatForm(processId) {
  //   return this.httpClient.get('/api/repository/form/paper-format/'.concat(processId));
  // }
  //
  // getPaperCorrectionTasks() {
  //   return this.httpClient.get('/api/repository/tasks/paper-correction');
  // }
  //
  // getChooseReviewerTasks() {
  //   return this.httpClient.get('/api/repository/tasks/choose-reviewer');
  // }
  //
  // getChooseReviwersForm(taskId) {
  //   return this.httpClient.get('/api/repository/form/choose-reviewers/'.concat(taskId));
  // }
  //
  // getChiefEditorReviewingTasks() {
  //   return this.httpClient.get('/api/repository/tasks/chief-editor-reviewing');
  // }
  //
  // getChiefEditorChoiceForm(processId)  {
  //   return this.httpClient.get('/api/repository/form/chief-editor-choice/'.concat(processId));
  // }
  //
  // getChiefEditorChoiceTasks() {
  //   return this.httpClient.get('/api/repository/tasks/chief-editor-choice');
  // }

}
