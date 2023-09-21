package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.Author;
import org.camunda.bpmn.service.MailService;
import org.camunda.bpmn.service.SciencePaperService;
import org.camunda.bpmn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyAboutRefusal implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Author author = (Author) userService.findOneByUsername((String) execution.getVariable("authorId"));
        sciencePaperService.remove(sciencePaperService.findOneById((Long) execution.getVariable("sciencePaperId")));
        mailService.notifyAboutRefusal(author);
    }
}
