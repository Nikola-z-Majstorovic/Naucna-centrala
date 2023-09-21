package org.camunda.bpmn.model;


import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends User{

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Membership> memberships;

}
