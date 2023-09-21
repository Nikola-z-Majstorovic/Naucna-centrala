package org.camunda.bpmn.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue("REVIEWER")
public class Reviewer extends User {

    @Column(name = "title")
    private String title;

    @ManyToMany(mappedBy = "reviewers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Magazine> magazines;

    public Reviewer() {
        super();
    }

    public Reviewer(User user){
        super(user);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Magazine> getMagazines() {
        return magazines;
    }

    public void setMagazines(List<Magazine> magazines) {
        this.magazines = magazines;
    }


}
