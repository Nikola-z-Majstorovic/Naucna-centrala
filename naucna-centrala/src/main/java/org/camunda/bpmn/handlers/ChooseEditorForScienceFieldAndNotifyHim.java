package org.camunda.bpmn.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpmn.model.*;
import org.camunda.bpmn.service.MagazineService;
import org.camunda.bpmn.service.MailService;
import org.camunda.bpmn.service.SciencePaperService;
import org.camunda.bpmn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ChooseEditorForScienceFieldAndNotifyHim implements JavaDelegate {


    @Autowired
    private UserService userService;

    @Autowired
    private SciencePaperService sciencePaperService;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private MailService mailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SciencePaper sciencePaper = sciencePaperService.findOneById((Long) execution.getVariable("sciencePaperId"));
        Magazine magazine = magazineService.findByName((String) execution.getVariable("magazineName"));
        List<Editor> editorList = magazine.getScienceFieldEditors();
        List<User> list = new ArrayList<>();
        for(Editor editor: editorList){
            for(ScienceField scienceField: editor.getScienceFields()){
                if(scienceField.getName().equals(sciencePaper.getScienceField().getName())){
                    list.add(editor);
                }
            }
        }
        if(!list.isEmpty()){
            int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
            execution.setVariable("izabrani_urednik", list.get(randomNum).getUsername());
        }else{
            execution.setVariable("izabrani_urednik", null);
        }
        Editor editor = (Editor) userService.findOneByUsername((String) execution.getVariable("izabrani_urednik"));
        if(editor != null){
            mailService.notifyScienceFieldEditor(editor);
        }
    }
}
