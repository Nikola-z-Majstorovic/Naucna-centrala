package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.Author;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.model.Membership;
import org.camunda.bpmn.security.TokenUtils;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CheckMembership implements JavaDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String magazineName = (String) delegateExecution.getVariable("magazineName");
        String username = (String) delegateExecution.getVariable("username");
        Magazine magazine = magazineService.findByName(magazineName);
//        delegateExecution.setVariable("uplacena_clanarina", false);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        for(Membership m: magazine.getMemberships()){
            if(m.getAuthor().getUsername().equals(username)){
                if(now.compareTo(m.getEndDate())> 0) {
                    delegateExecution.setVariable("uplacena_clanarina", true);
                }
            }
        }
    }
}

