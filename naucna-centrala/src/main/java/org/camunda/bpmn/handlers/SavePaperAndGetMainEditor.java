package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.model.Coauthor;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.model.SciencePaper;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.SciencePaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavePaperAndGetMainEditor implements JavaDelegate {

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        //cuvanje naucnog rada
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) execution.getVariable("sciencePaperId"));
        List<FormSubmissionDto> sciencePaperData = (List<FormSubmissionDto>) execution.getVariable("sciencePaperData");
        ArrayList<Coauthor> coauthors = (ArrayList<Coauthor>) execution.getVariable("coauthorList");
        sciencePaper = sciencePaperService.create(sciencePaper, sciencePaperData, coauthors);

        // odabir glavnog urednika
        String magazineName = (String) execution.getVariable("magazineName");
        Magazine magazine = magazineService.findByName(magazineName);
        execution.setVariable("chiefEditor", magazine.getChiefEditor().getUsername());

    }

}
