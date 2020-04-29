package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.Author;
import org.camunda.bpmn.service.MailService;
import org.camunda.bpmn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorrectionNotification implements JavaDelegate {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Author author = (Author) userService.findOneByUsername((String) execution.getVariable("authorId"));
        mailService.correctionNotification(author);
    }
}
