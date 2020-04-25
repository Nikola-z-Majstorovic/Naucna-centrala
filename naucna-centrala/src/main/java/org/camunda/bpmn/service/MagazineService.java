package org.camunda.bpmn.service;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpmn.dto.MagazineDTO;
import org.camunda.bpmn.model.Magazine;
import org.camunda.bpmn.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MagazineService {

    @Autowired
    private MagazineRepository magazineRepo;

	
    public List<MagazineDTO> findAll(){
        List<MagazineDTO> magazines = new ArrayList<>();
        for(Magazine magazine: magazineRepo.findAll()){
//            if(magazine.getSellerId() != null){
//                magazines.add(new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(),
//                        magazine.getScienceFields(), magazine.getChiefEditor(), magazine.isRegistered(), magazine.getSellerId(), magazine.getSciencePapers()));
//            }else{
                magazines.add(new MagazineDTO(magazine.getId(), magazine.getName(), magazine.getIssn(),
                        magazine.getScienceFields(), magazine.getChiefEditor(), magazine.isRegistered(), new Long(0), magazine.getSciencePapers()));

//            }
        }
        return magazines;
    }
    public Magazine findByName(String name){
        return magazineRepo.findByName(name);
    }


}
