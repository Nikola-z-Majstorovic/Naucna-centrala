package org.camunda.bpmn.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpmn.dto.AccessAndMembershipDTO;
import org.camunda.bpmn.dto.FormFieldsDto;
import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.dto.MagazineDTO;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "magazine")
public class MagazineController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private TokenUtils tokenUtils;

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
                System.out.println(((EnumFormType) field.getType()).getValues());
                for(MagazineDTO magazineDTO: magazines){
                    enumType.getValues().put(magazineDTO.getName(), magazineDTO.getName());

                }
                break;
            }
        }
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/select-magazine/{taskId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<AccessAndMembershipDTO> selectMagazine(@RequestBody List<FormSubmissionDto> magazine, @PathVariable("taskId") String taskId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(magazine);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String name = Utils.getUsernameFromRequest(request, tokenUtils);

        runtimeService.setVariable(processInstanceId, "magazineName", Utils.getFormFieldValue(magazine, "casopis"));
        runtimeService.setVariable(processInstanceId, "username", name);

        formService.submitTaskForm(taskId, map);
        boolean openAccess = (boolean) runtimeService.getVariable(processInstanceId, "open_access");
        boolean membership = (boolean) runtimeService.getVariable(processInstanceId, "uplacena_clanarina");
        return new ResponseEntity<>(new AccessAndMembershipDTO(openAccess,membership), HttpStatus.OK);
    }



}
