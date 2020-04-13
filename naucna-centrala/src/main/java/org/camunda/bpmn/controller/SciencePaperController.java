package org.camunda.bpmn.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpmn.dto.FormFieldsDto;
import org.camunda.bpmn.dto.MagazineDTO;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/science-paper")
public class SciencePaperController {

	
    @Autowired
    TaskService taskService;
    
    @Autowired
    FormService formService;
    
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TokenUtils tokenUtils;
    
    @Autowired
    private MagazineService magazineService;

    
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getForm(HttpServletRequest request){
    	
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("Obrada");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        runtimeService.setVariable(pi.getId(), "authorId", Utils.getUsernameFromRequest(request, tokenUtils));

        List<MagazineDTO> magazines = magazineService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("casopis")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(MagazineDTO magazineDTO: magazines){
                    enumType.getValues().put(magazineDTO.getName(), magazineDTO.getName());
                }
                break;
            }
        }
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }
}
