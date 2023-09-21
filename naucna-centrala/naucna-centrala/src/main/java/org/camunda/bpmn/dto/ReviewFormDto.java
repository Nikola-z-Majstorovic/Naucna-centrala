package org.camunda.bpmn.dto;

import java.io.Serializable;


public class ReviewFormDto implements Serializable{

    private String komentarUredniku;

    private String preporukaPrihvacanja;

    private String komentarAutoru;

    private String username;


    public ReviewFormDto() {
    }

    public String getKomentarUredniku() {
        return komentarUredniku;
    }

    public void setKomentarUredniku(String komentarUredniku) {
        this.komentarUredniku = komentarUredniku;
    }

    public String getPreporukaPrihvacanja() {
        return preporukaPrihvacanja;
    }

    public void setPreporukaPrihvacanja(String preporukaPrihvacanja) {
        this.preporukaPrihvacanja = preporukaPrihvacanja;
    }

    public String getKomentarAutoru() {
        return komentarAutoru;
    }

    public void setKomentarAutoru(String komentarAutoru) {
        this.komentarAutoru = komentarAutoru;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //    @Override
//    public String getId() {
//        return null;
//    }
//
//    @Override
//    public String getLabel() {
//        return null;
//    }
//
//    @Override
//    public FormType getType() {
//        return null;
//    }
//
//    @Override
//    public String getTypeName() {
//        return null;
//    }
//
//    @Override
//    public Object getDefaultValue() {
//        return null;
//    }
//
//    @Override
//    public TypedValue getValue() {
//        return null;
//    }
//
//    @Override
//    public List<FormFieldValidationConstraint> getValidationConstraints() {
//        return null;
//    }
//
//    @Override
//    public Map<String, String> getProperties() {
//        return null;
//    }
//
//    @Override
//    public boolean isBusinessKey() {
//        return false;
//    }
}



