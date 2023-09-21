package org.camunda.bpmn.dto;

import org.camunda.bpm.engine.form.FormField;

import java.util.List;

public class FormFieldsReviewsDto {

    private List<FormField> formFields;
    private List<ReviewFormDto> reviewersForm;
    private String processInstanceId;
    private String id;

    public FormFieldsReviewsDto() {

    }

    public FormFieldsReviewsDto(String id, String processInstanceId, List<FormField> properties, List<ReviewFormDto> reviewersForm) {
        this.id = id;
        this.processInstanceId  = processInstanceId;
        this.formFields = properties;
        this.reviewersForm = reviewersForm;
    }

    public List<FormField> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormField> formFields) {
        this.formFields = formFields;
    }

    public List<ReviewFormDto> getReviewersForm() {
        return reviewersForm;
    }

    public void setReviewersForm(List<ReviewFormDto> reviewersForm) {
        this.reviewersForm = reviewersForm;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
