package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class AssignChiefEditor implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee((String) delegateTask.getExecution().getVariable("chiefEditor"));
    }
}
