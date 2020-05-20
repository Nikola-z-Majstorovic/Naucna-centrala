package org.camunda.bpmn.controller;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormFieldValidationConstraint;
import org.camunda.bpm.engine.form.FormType;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpmn.dto.*;
import org.camunda.bpmn.model.*;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.ReviewerService;
import org.camunda.bpmn.service.UserService;
import org.camunda.bpmn.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "users")
public class UserController {
	
    @Autowired
    public TokenUtils tokenUtils;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    ReviewerService reviewerService;
    
    @RequestMapping(value = "/get-user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDTO> getUser(HttpServletRequest request){
        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        UserInfoDTO ui = new UserInfoDTO();
        if(username != "" && username != null) {
            User u = (User) userService.findOneByUsername(username);
            if(u instanceof Admin){
                u = (Admin) u;
                ui.setRole("ADMIN");
            }else if(u instanceof Reviewer){
                u = (Reviewer) u;
                ui.setRole("REVIEWER");
            }else if(u instanceof Editor){
                u = (Editor) u;
                ui.setRole("EDITOR");
            }else if(u instanceof  Author){
                u = (Author) u;
                ui.setRole("AUTHOR");
            }
            ui.setUsername(u.getUsername());
            return new ResponseEntity<UserInfoDTO>(ui, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping( value = "/{taskId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveReview(@RequestBody List<FormSubmissionDto> reviewForm, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(reviewForm);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String username = task.getAssignee();
        Reviewer reviewer = (Reviewer) userService.findOneByUsername(username);
        List<ReviewFormDto> reviewersForm = (ArrayList<ReviewFormDto>) runtimeService.getVariable(processInstanceId,"reviewersForm");
        ReviewFormDto reviewFormDto = reviewerService.create(reviewForm,reviewer.getUsername());
        reviewersForm.add(reviewFormDto);
        runtimeService.setVariable(processInstanceId, "reviewersForm", reviewersForm);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }



    @RequestMapping(value = "/chief-or-editor-review/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> chiefOrEditorReview(@RequestBody List<FormSubmissionDto> chiefEditorReviewData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(chiefEditorReviewData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId,  "chiefEditorReviewData", chiefEditorReviewData);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}
