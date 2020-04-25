package org.camunda.bpmn.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpmn.dto.FormFieldsDto;
import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "payment")
public class PaymentController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    private TokenUtils tokenUtils;


    @RequestMapping(value = "/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getForm(@PathVariable("processInstanceId") String processInstanceId){
        ProcessInstance subprocess = runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(subprocess.getId()).list().get(0);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), processInstanceId, properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/submit/{taskId}/{processInstanceId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> submit(@RequestBody List<FormSubmissionDto> paymentData, @PathVariable("taskId") String taskId, @PathVariable("processInstanceId") String processInstanceId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(paymentData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String subProcessInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(subProcessInstanceId, "payment", Utils.getFormFieldValue(paymentData, "iznos_clanarine"));

//        String name = Utils.getUsernameFromRequest(request, tokenUtils);
//        runtimeService.setVariable(processInstanceId, "username", name);
//        runtimeService.setVariable(subProcessInstanceId,"clanarina_uplacena",false);
//
        formService.submitTaskForm(taskId, map);

//        boolean clanarina_uplacena = (boolean) runtimeService.getVariable(processInstanceId, "clanarina_uplacena");
        return new ResponseEntity<>(true, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(false, HttpStatus.OK);
//        }
    }
}
