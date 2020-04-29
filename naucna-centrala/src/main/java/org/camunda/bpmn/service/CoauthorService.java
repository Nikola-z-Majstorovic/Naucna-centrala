package org.camunda.bpmn.service;

import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.model.Coauthor;
import org.camunda.bpmn.repository.CoauthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoauthorService {

    @Autowired
    private CoauthorRepository coauthorRepo;

    public Coauthor findOneById(Long id){
        return coauthorRepo.findOneById(id);
    }

    public Coauthor create(List<FormSubmissionDto> coauthorData) {
        Coauthor coauthor = new Coauthor();
        for(FormSubmissionDto dto: coauthorData){
            if(dto.getFieldId().equals("ime_koautora")){
                coauthor.setFristName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("prezime_koautora")){
                coauthor.setLastName(dto.getFieldValue());
            }else if(dto.getFieldId().equals("email_koautora")){
                coauthor.setEmail(dto.getFieldValue());
            }else if(dto.getFieldId().equals("grad")){
                coauthor.setCity(dto.getFieldValue());
            }else if(dto.getFieldId().equals("drzava")){
                coauthor.setCountry(dto.getFieldValue());
            }
        }
        return coauthorRepo.save(coauthor);
    }
}
