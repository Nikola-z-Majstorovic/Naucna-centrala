package org.camunda.bpmn.service;

import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.dto.ReviewFormDto;
import org.camunda.bpmn.model.Coauthor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewerService {

    public ReviewFormDto create(List<FormSubmissionDto> reviewForm,String username) {
        ReviewFormDto reviewFormDto = new ReviewFormDto();
        for(FormSubmissionDto dto: reviewForm){
            if(dto.getFieldId().equals("komentar_uredniku")){
                reviewFormDto.setKomentarUredniku(dto.getFieldValue());
            }else if(dto.getFieldId().equals("preporuka_prihvacanja")){
                reviewFormDto.setPreporukaPrihvacanja(dto.getFieldValue());
            }else if(dto.getFieldId().equals("komentar_autoru")) {
                reviewFormDto.setKomentarAutoru(dto.getFieldValue());
            }
        }
        reviewFormDto.setUsername(username);
        return reviewFormDto;
    }


}
