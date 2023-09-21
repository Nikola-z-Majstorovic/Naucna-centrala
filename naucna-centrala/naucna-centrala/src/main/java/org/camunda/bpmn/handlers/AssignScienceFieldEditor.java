package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service
public class AssignScienceFieldEditor implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("*****************************************");
        System.out.println("Asignee za izbor recenzenata: " + delegateTask.getExecution().getVariable("izabrani_urednik"));
        System.out.println("*****************************************");
        System.out.println("*****************************************");
        if(delegateTask.getExecution().getVariable("izabrani_urednik") == null ) {
        System.out.println("Asignee za izbor recenzenata od glavnog urednika: " + delegateTask.getExecution().getVariable("izabrani_glavni_urednik"));
        System.out.println("*****************************************");
            delegateTask.setAssignee((String) delegateTask.getExecution().getVariable("izabrani_glavni_urednik"));
        }
        else {
            delegateTask.setAssignee((String) delegateTask.getExecution().getVariable("izabrani_urednik"));
        }
    }
}
