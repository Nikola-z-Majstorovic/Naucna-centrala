package org.camunda.bpmn.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpmn.dto.FormFieldsDto;
import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.dto.ReviewFormDto;
import org.camunda.bpmn.dto.TaskDto;
import org.camunda.bpmn.model.Coauthor;
import org.camunda.bpmn.model.ScienceField;
import org.camunda.bpmn.model.SciencePaper;
import org.camunda.bpmn.model.User;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "task")
public class TaskController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoauthorService coauthorService;


    @RequestMapping(value = "/coauthor", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity addCoauthorTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Izbor koautora").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/claim/{taskId}", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity claim(@PathVariable String taskId, HttpServletRequest request) {
        String user = Utils.getUsernameFromRequest(request, tokenUtils);
        taskService.claim(taskId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/form/{taskId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDto> getFormFields(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), processInstanceId, properties), HttpStatus.OK);
    }

    @RequestMapping( value = "/{taskId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveCoauthor(@RequestBody List<FormSubmissionDto> coauthorData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(coauthorData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        ArrayList<Coauthor> coauthors = (ArrayList<Coauthor>) runtimeService.getVariable(processInstanceId,"coauthorList");
        Coauthor coauthor = coauthorService.create(coauthorData);
        coauthors.add(coauthor);
        runtimeService.setVariable(processInstanceId, "coauthorList", coauthors);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    @RequestMapping(value = "/paper-correction", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity paperCorrectionTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Iznena naucnog rada").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/review-paper", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity reviewPaperTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Zadatak za glavnog urednika koji pregleda casopis").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }
    @RequestMapping(value = "/paper-review/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> paperReview(@RequestBody List<FormSubmissionDto> reviewPaperData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(reviewPaperData);
        formService.submitTaskForm(taskId, map);
        String value = Utils.getFormFieldValue(reviewPaperData, "relevantnost_rada");
        if(value.equals("ne")){
            return new ResponseEntity<>("Rad nije relevantan.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Rad je relevantan.", HttpStatus.OK);
    }
    @RequestMapping(value = "/choose-reviewer",  method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity chooseReviewerTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Zadatak za glavnog urednika ili urednika" +
                " naucne oblasti da izabere recezente").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/form/choose-reviewers/{taskId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getChooseReviwersForm(@PathVariable("taskId") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) runtimeService.getVariable(pi.getId(), "sciencePaperId"));
        List<User> reviewers = userService.findAllReviewers();
        List<User> reviewerList = new ArrayList<>();
        for (User reviewer : reviewers) {
            for (ScienceField scienceField : reviewer.getScienceFields()) {
                if (scienceField.getName().equals(sciencePaper.getScienceField().getName())) {
                    reviewerList.add(reviewer);
                }
            }
        }
        List<FormField> properties = tfd.getFormFields();
        for (FormField field : properties) {
            if (field.getId().equals("recenzenti")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for (User user : reviewerList) {
                    enumType.getValues().put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
                }
            }
        }
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/choose-reviewers/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> chooseReviewers(@RequestBody List<FormSubmissionDto> reviewersData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(reviewersData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId,  "reviewersData", reviewersData);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/paper-review", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity paperReviewTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Rezultati recenzije").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }
    @RequestMapping(value = "/chief-or-editor-choice", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity chiefEditorChoiceTasks(HttpServletRequest request){
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Glavi urednik ili " +
                "urednik naucne oblasti odlucuje da li prihvata rad").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }
    @RequestMapping(value = "/chief-or-editor-choice/{taskId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> chiefOrEditorChoiceForm(@PathVariable("taskId") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), processInstanceId, properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/paper-big-correction", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity paperBigCorrectionTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Autor koriguje izmene").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/choose-time-error", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity chooseTimeError(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Odredjivanje vremena za ispravku rada").taskAssignee(username).list();
        List<TaskDto> tasksDto = new ArrayList<>();
        for(Task task: tasks){
            TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/addingTime/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> paperFormat(@RequestBody List<FormSubmissionDto> paperFormatData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(paperFormatData);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}
