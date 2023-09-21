package org.camunda.bpmn.dto;

import org.camunda.bpmn.model.Editor;
import org.camunda.bpmn.model.ScienceField;
import org.camunda.bpmn.model.SciencePaper;

import java.util.ArrayList;
import java.util.List;

public class MagazineDTO {
	 private Long id;
	    private String name;
	    private String issn;
	    private boolean isRegistered;
	    private Long sellerId;
	    private List<String> scienceFieldList = new ArrayList<>();
	    private String chiefEditor;
//	    private List<SciencePaperDTO> sciencePaperDTOList = new ArrayList<>();

	    public MagazineDTO() {
	    }

	    public MagazineDTO(Long id, String name, String issn, List<ScienceField> scienceFieldList, Editor chiefEditor,
	                       boolean isRegistered, long sellerId
	                       ,List<SciencePaper> sciencePapers
	                       ) {
	        this.id = id;
	        this.name = name;
	        this.issn = issn;
	        this.setScienceFieldList(scienceFieldList);
	        this.setChiefEditor(chiefEditor);
	        this.isRegistered = isRegistered;
	        this.setSellerId(sellerId);
//	        this.setSciencePaperDTOList(sciencePapers);
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getIssn() {
	        return issn;
	    }

	    public void setIssn(String issn) {
	        this.issn = issn;
	    }

	    public List<String> getScienceFieldList() {
	        return scienceFieldList;
	    }

	    public boolean isRegistered() {
	        return isRegistered;
	    }

	    public void setRegistered(boolean registered) {
	        isRegistered = registered;
	    }

	    public void setScienceFieldList(List<ScienceField> scienceFieldList) {
	        for(ScienceField scienceField: scienceFieldList){
	            this.scienceFieldList.add(scienceField.getName());
	        }
	    }
	    public String getChiefEditor() {
	        return chiefEditor;
	    }
	    public void setChiefEditor(Editor chiefEditor) {
	        this.chiefEditor = chiefEditor.getFirstName() + " " + chiefEditor.getLastName();
	    }

	    public Long getSellerId() {
	        return sellerId;
	    }

	    public void setSellerId(Long sellerId) {
	        this.sellerId = sellerId;
	    }

	    public void setChiefEditor(String chiefEditor) {
	        this.chiefEditor = chiefEditor;
	    }

//	    public void setSciencePaperDTOList(List<SciencePaper> sciencePapers) {
//	        for(SciencePaper paper: sciencePapers){
//	            this.sciencePaperDTOList.add(new SciencePaperDTO(paper.getId(), paper.getTitle(), paper.getKeyTerm(),
//	                    paper.getPaperAbstract(), paper.getPrice(), paper.getCurrency(), null));
//	        }
//	    }
//
//	    public List<SciencePaperDTO> getSciencePaperDTOList() {
//	        return sciencePaperDTOList;
//	    }
	
}
