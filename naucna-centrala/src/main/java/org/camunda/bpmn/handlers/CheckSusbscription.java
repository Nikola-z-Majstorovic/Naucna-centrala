package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//        String magazineName = (String) execution.getVariable("magazineName");
//        String username = (String) execution.getVariable("username");

//        Magazine magazine = magazineService.findByName(magazineName);
@Service
public class CheckSusbscription  implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    TaskService taskService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String processInstanceId = ((ExecutionEntity) execution).getRootProcessInstanceId();

        Long subsription = (Long) execution.getVariable("iznos_clanarine");

        if(subsription<10) {
            execution.setVariable("clanarina_uplacena",false);
        }else {
            execution.setVariable("clanarina_uplacena",true);
        }

    }
}
