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
import org.camunda.bpmn.dto.TaskDto;
import org.camunda.bpmn.model.Coauthor;
import org.camunda.bpmn.model.ScienceField;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.CoauthorService;
import org.camunda.bpmn.service.ScienceFieldService;
import org.camunda.bpmn.service.Utils;
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
    private ScienceFieldService scienceFieldService;

    @Autowired
    private TokenUtils tokenUtils;

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
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
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
}
