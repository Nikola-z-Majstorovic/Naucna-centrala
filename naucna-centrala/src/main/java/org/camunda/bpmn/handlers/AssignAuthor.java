package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;


@Service
public class AssignAuthor implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee((String) delegateTask.getExecution().getVariable("authorId"));
    }
}
