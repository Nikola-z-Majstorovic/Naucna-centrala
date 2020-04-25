package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.*;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.SciencePaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckReviewers implements JavaDelegate {

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long)execution.getVariable("sciencePaperId"));
        Magazine magazine = magazineService.findByName((String) execution.getVariable("magazineName"));
        List<Reviewer> reviewerList = magazine.getReviewers();
        List<User> list = new ArrayList<>();
        for(Reviewer reviewer: reviewerList){
            for(ScienceField scienceField: reviewer.getScienceFields()){
                if(scienceField.getName().equals(sciencePaper.getScienceField().getName())){
                    list.add(reviewer);
                }
            }
        }
        if(list.size()< 2) {
            execution.setVariable("broj_recezenata", null);
        } else {
            execution.setVariable("broj_recezenata", list);
        }
    }
}
