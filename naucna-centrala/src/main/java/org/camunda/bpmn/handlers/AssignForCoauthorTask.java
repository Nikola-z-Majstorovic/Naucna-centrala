package org.camunda.bpmn.handlers;


import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignForCoauthorTask implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee((String) delegateTask.getExecution().getVariable("authorId"));
    }
}
