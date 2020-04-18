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
import org.camunda.bpmn.model.ScienceField;
import org.camunda.bpmn.service.ScienceFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/form/science-paper/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getSciencePaperForm(@PathVariable("processInstanceId") String processInstanceId){
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        List<ScienceField> scienceFields = scienceFieldService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("naucna_oblast")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(ScienceField scienceField: scienceFields){
                    enumType.getValues().put(scienceField.getName(), scienceField.getName());
                }
            }
        }
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }
}
