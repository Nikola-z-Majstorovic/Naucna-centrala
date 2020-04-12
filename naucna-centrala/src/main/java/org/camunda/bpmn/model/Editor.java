package org.camunda.bpmn.model;

import org.hibernate.validator.constraints.ScriptAssert;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("EDITOR")
public class Editor extends User {

    @Column(name = "title")
    private String title;

    @ManyToOne
    private Magazine magazine;

    public Editor(){ super(); }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

}
