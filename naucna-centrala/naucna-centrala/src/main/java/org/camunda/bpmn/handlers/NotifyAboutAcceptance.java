package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.Author;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.model.SciencePaper;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.MailService;
import org.camunda.bpmn.service.SciencePaperService;
import org.camunda.bpmn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyAboutAcceptance implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Author author = (Author) userService.findOneByUsername((String) execution.getVariable("authorId"));
        mailService.notifyAboutAcceptance(author);
        Magazine magazine = magazineService.findByName((String) execution.getVariable("magazineName"));
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) execution.getVariable("sciencePaperId"));
        magazine.addSciencePaper(sciencePaper);
        magazine = magazineService.save(magazine);
        System.out.println("********************************************");
        System.out.println("DODAVANJE RADA U CASOPIS " + magazine.getName());
        System.out.println("Rad " + sciencePaper.getTitle() + " dodat u casopis " +sciencePaper.getMagazine());
        System.out.println("********************************************");
    }

}
