package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IsOpenAccess implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    TaskService taskService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String magazineName = (String) delegateExecution.getVariable("magazineName");
        Magazine magazine = magazineService.findByName(magazineName);
        if (magazine.getBillingType().name().equals("AUTHORS")) {
            delegateExecution.setVariable("open_access", true);
        } else {
            delegateExecution.setVariable("open_access", false);
        }
        delegateExecution.setVariable("uplacena_clanarina", false);
    }
}
