package org.camunda.bpmn.controller;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpmn.dto.FormFieldsDto;
import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.model.Coauthor;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.model.ScienceField;
import org.camunda.bpmn.model.SciencePaper;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.ScienceFieldService;
import org.camunda.bpmn.service.SciencePaperService;
import org.camunda.bpmn.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/science-paper")
public class SciencePaperController {


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;
    
    @Autowired
    FormService formService;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private MagazineService magazineService;

    @RequestMapping(value = "/form/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getSciencePaperForm(@PathVariable("processInstanceId") String processInstanceId){
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        Magazine magazine = magazineService.findByName((String)runtimeService.getVariable(processInstanceId,"magazineName"));
        List<ScienceField> scienceFields = magazine.getScienceFields();
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

    @RequestMapping(value = "/{taskId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> save(@RequestBody List<FormSubmissionDto> sciencePaperData, @PathVariable("taskId") String taskId, HttpServletRequest request){
        HashMap<String, Object> map = Utils.mapListToDto(sciencePaperData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        SciencePaper sciencePaper = new SciencePaper();
        sciencePaper.setPdfName(Utils.getFormFieldValue(sciencePaperData, "pdf"));
        sciencePaper = sciencePaperService.save(sciencePaper);

        runtimeService.setVariable(processInstanceId, "sciencePaperData", sciencePaperData);
        runtimeService.setVariable(processInstanceId, "sciencePaperId", sciencePaper.getId());
        runtimeService.setVariable(processInstanceId, "coauthorList", new ArrayList<Coauthor>());

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(sciencePaper.getId().toString(), HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> savePdf(@RequestParam("file") MultipartFile file , @PathVariable("id") String sciencePaperId){
        SciencePaper sciencePaper = sciencePaperService.findOneById(Long.parseLong(sciencePaperId));
        sciencePaperService.savePdf(file, sciencePaper);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/form/paper-format/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FormFieldsDto> getPaperFormatForm(@PathVariable("processInstanceId") String processInstanceId){
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new ResponseEntity<>(new FormFieldsDto(task.getId(), pi.getId(), properties), HttpStatus.OK);
    }

    @RequestMapping(value = "/paper-format/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> paperFormat(@RequestBody List<FormSubmissionDto> paperFormatData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(paperFormatData);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity("Success", HttpStatus.OK);
    }
    @RequestMapping(value = "/paper-correction/{taskId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> paperCorrection(@RequestBody List<FormSubmissionDto> paperCorrectionData, @PathVariable("taskId") String taskId){
        HashMap<String, Object> map = Utils.mapListToDto(paperCorrectionData);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity(runtimeService.getVariable(processInstanceId, "sciencePaperId"), HttpStatus.OK);
    }

}
