package org.camunda.bpmn.dto;


import java.io.Serializable;

public class ReviewFormDto implements Serializable {

    private String komentarUredniku;

    private String preporukaPrihvacanja;

    private String komentarAutoru;

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
}



