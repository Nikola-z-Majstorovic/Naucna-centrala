package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.Editor;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.model.Reviewer;
import org.camunda.bpmn.model.User;
import org.camunda.bpmn.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllReviewers implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String magazineName = (String) execution.getVariable("magazineName");
        Magazine magazine = magazineService.findByName(magazineName);
        execution.setVariable("izabrani_urednik", magazine.getChiefEditor().getUsername());
        List<Reviewer> reviewerList = magazine.getReviewers();
        List<User> list = new ArrayList<>();
        for(Reviewer reviewer: reviewerList){
            list.add(reviewer);
        }
        execution.setVariable("broj_recezenata", list);
    }
}
