package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.dto.FormSubmissionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaveReviewers implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDto> reviewersData = (List<FormSubmissionDto>) execution.getVariable("reviewersData");
        List<String> reviewerList = new ArrayList<>();
        for(FormSubmissionDto dto: reviewersData){
            reviewerList.add(dto.getFieldValue());
        }
        execution.setVariable("reviewerList", reviewerList);
    }
}
