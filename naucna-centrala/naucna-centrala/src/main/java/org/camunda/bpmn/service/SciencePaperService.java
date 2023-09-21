package org.camunda.bpmn.service;


import org.camunda.bpmn.dto.FormSubmissionDto;
import org.camunda.bpmn.model.Coauthor;
import org.camunda.bpmn.model.ScienceField;
import org.camunda.bpmn.model.SciencePaper;
import org.camunda.bpmn.repository.CoauthorRepository;
import org.camunda.bpmn.repository.SciencePaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SciencePaperService {

    @Autowired
    SciencePaperRepository sciencePaperRepository;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @Autowired
    private CoauthorService coauthorService;

    @Autowired
    private CoauthorRepository coauthorRepo;

    public SciencePaper create(SciencePaper sciencePaper, List<FormSubmissionDto> sciencePaperData, ArrayList<Coauthor> coauthors) {
        for(FormSubmissionDto dto: sciencePaperData){
            if(dto.getFieldId().equals("naslov_rada")){
                sciencePaper.setTitle(dto.getFieldValue());
            }else if(dto.getFieldId().equals("apstrakt")){
                sciencePaper.setPaperAbstract(dto.getFieldValue());
            }else if(dto.getFieldId().equals("kljucni_pojam")){
                sciencePaper.setKeyTerm(dto.getFieldValue());
            }else if(dto.getFieldId().equals("naucna_oblast")){
                ScienceField scienceField = scienceFieldService.findOneByName(dto.getFieldValue());
                sciencePaper.setScienceField(scienceField);
            }
        }
        for(Coauthor coauthor: coauthors){
            Coauthor coauthorTemp = coauthorService.findOneById(coauthor.getId());
            sciencePaper.addCoauthor(coauthorTemp);
        }
        sciencePaper = sciencePaperRepository.save(sciencePaper);
        return sciencePaper;
    }

    public SciencePaper findOneById(Long sciencePaperId) {
        return sciencePaperRepository.findOneById(sciencePaperId);
    }

    public SciencePaper savePdf(MultipartFile file, SciencePaper sciencePaper) {
        try {
            sciencePaper.setPdf(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sciencePaperRepository.save(sciencePaper);
    }

    public SciencePaper save(SciencePaper sciencePaper) {
        return sciencePaperRepository.save(sciencePaper);
    }

    public void remove(SciencePaper sciencePaper) {
        sciencePaperRepository.delete(sciencePaper);
    }
}
