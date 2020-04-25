package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.Author;
import org.camunda.bpmn.model.Editor;
import org.camunda.bpmn.service.MailService;
import org.camunda.bpmn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMailAuthorAndEditor implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String editor = (String) execution.getVariable("chiefEditor");
        Editor chiefEditor = (Editor) userService.findOneByUsername(editor);
        Author author = (Author) userService.findOneByUsername((String) execution.getVariable("authorId"));
        mailService.sendNotification(author, chiefEditor);
    }

}
