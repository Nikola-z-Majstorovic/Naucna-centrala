package org.camunda.bpmn.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpmn.dto.*;
import org.camunda.bpmn.model.*;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private MagazineService magazineService;

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

    @RequestMapping(value = "/reviewerForm/{taskId}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsReviewsDto> getReviewerFormFields(@PathVariable("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        List<ReviewFormDto> reviewersForm = (ArrayList<ReviewFormDto>) runtimeService.getVariable(processInstanceId, "reviewersForm");
        return new ResponseEntity<>(new FormFieldsReviewsDto(task.getId(), processInstanceId, properties,reviewersForm), HttpStatus.OK);
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

    @RequestMapping(value = "/choose-other-reviewer",  method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity chooseOtherReviewerTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Biranje drugog recenznenta").taskAssignee(username).list();
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
        List<Reviewer> reviewers = (List<Reviewer>) runtimeService.getVariable(task.getProcessInstanceId(),"broj_recezenata");;
        List<User> reviewerList = new ArrayList<>();
        //////////// ovo se ne izvrsava ukoliko glavni urednik dobije rolu urednika naucne oblasti
        if(runtimeService.getVariable(task.getProcessInstanceId(),"izabrani_glavni_urednik")== null) {
            for (User reviewer : reviewers) {
                for (ScienceField scienceField : reviewer.getScienceFields()) {
                    if (scienceField.getName().equals(sciencePaper.getScienceField().getName())) {
                        reviewerList.add(reviewer);
                    }
                }
            }
        } else {
            for (User reviewer : reviewers) {
                        reviewerList.add(reviewer);
                    }
        }
        List<FormField> properties = tfd.getFormFields();
        for (FormField field : properties) {
            if (field.getId().equals("recenzenti") || field.getId().equals("recenzent")) {
                Map<String, String> enumType = ((EnumFormType) field.getType()).getValues();
                enumType.clear();
                for (User user : reviewerList) {
                    enumType.put(user.getUsername(), user.getFirstName() + " " + user.getLastName() + ", " + user.getUsername());
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
        List<FormSubmissionDto> formReviewersData = new ArrayList<>();
        for (FormSubmissionDto field : reviewersData) {
            if (field.getFieldId().equals("recenzenti")) {
                formReviewersData.add(field);
            }
        }
        runtimeService.setVariable(processInstanceId,  "reviewersData", formReviewersData);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity("Success", HttpStatus.OK);
    }
    @RequestMapping(value = "/choose-other-reviewers/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> chooseOtherReviewers(@RequestBody List<FormSubmissionDto> reviewersData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(reviewersData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String subProcessInstanceId = task.getProcessInstanceId();
        String reviewer = "";
        for (FormSubmissionDto field : reviewersData) {
            if (field.getFieldId().equals("recenzent")) {
                    reviewer = field.getFieldValue();
            }
        }
        runtimeService.setVariable(subProcessInstanceId,  "reviewer", reviewer);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity("Success", HttpStatus.OK);
    }
    @RequestMapping(value = "/paper-review", method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity paperReviewTasks(HttpServletRequest request) {
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        List<Task> tasks = taskService.createTaskQuery().taskName("Recenziranje").taskAssignee(username).list();
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

    @RequestMapping(value = "/chief-or-editor-choice-and-author-correction/{taskId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsReviewsDto> chiefOrEditorChoiceForm(@PathVariable("taskId") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        List<ReviewFormDto> reviewersForm = (ArrayList<ReviewFormDto>) runtimeService.getVariable(processInstanceId, "reviewersForm");
        return new ResponseEntity<>(new FormFieldsReviewsDto(task.getId(), processInstanceId, properties,reviewersForm), HttpStatus.OK);
    }




    @RequestMapping(value = "/addingTime/{taskId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> paperFormat(@RequestBody List<FormSubmissionDto> paperFormatData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(paperFormatData);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}