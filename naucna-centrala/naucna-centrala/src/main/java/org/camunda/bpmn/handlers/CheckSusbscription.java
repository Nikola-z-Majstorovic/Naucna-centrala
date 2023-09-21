package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class CheckSusbscription  implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long subsription = (Long) execution.getVariable("iznos_clanarine");

        if(subsription<10) {
            execution.setVariable("clanarina_uplacena",false);
        }else {
            execution.setVariable("clanarina_uplacena",true);
        }
    }
}
