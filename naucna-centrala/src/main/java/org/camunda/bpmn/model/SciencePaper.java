package org.camunda.bpmn.model;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SciencePaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "key_term")
    private String keyTerm;

    @Column(name = "paper_abstract")
    private String paperAbstract;

    @Column(name = "pdf")
    @Lob
    private byte[] pdf;

    @Column(name = "currency")
    private String currency;

    @Column(name = "price")
    private double price;

    @Column(name = "pdf_name")
    private String pdfName;

    @ManyToOne
    private ScienceField scienceField;

    @ManyToOne
    private Magazine magazine;

//    @OneToMany(mappedBy = "sciencePaper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<OrderObject> orderObjects = new ArrayList<>();

    @OneToMany(mappedBy = "sciencePaper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Coauthor> coauthors = new ArrayList<Coauthor>();

    public SciencePaper() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyTerm() {
        return keyTerm;
    }

    public void setKeyTerm(String keyTerm) {
        this.keyTerm = keyTerm;
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public ScienceField getScienceField() {
        return scienceField;
    }

    public void setScienceField(ScienceField scienceField) {
        this.scienceField = scienceField;
    }

//    public List<OrderObject> getOrderObjects() {
//        return orderObjects;
//    }
//
//    public void setOrderObjects(List<OrderObject> orderObjects) {
//        this.orderObjects = orderObjects;
//    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public List<Coauthor> getCoauthors() {
        return coauthors;
    }

    public void setCoauthors(List<Coauthor> coauthors) {
        this.coauthors = coauthors;
    }

    public void addCoauthor(Coauthor coauthor){
        this.coauthors.add(coauthor);
        coauthor.setSciencePaper(this);
    }
}