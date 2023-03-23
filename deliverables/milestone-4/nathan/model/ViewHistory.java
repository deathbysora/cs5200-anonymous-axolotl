package BeerApp.model;

import java.util.Date;

public class ViewHistory {
    protected int viewId;
    protected Date created;
    protected Persons person;
    protected Beer beer;
    
    public ViewHistory(Date created, Persons person, Beer beer) {
        this.created = created;
        this.person = person;
        this.beer = beer;
    }

    public ViewHistory(int viewId, Date created, Persons person, Beer beer) {
        this.viewId = viewId;
        this.created = created;
        this.person = person;
        this.beer = beer;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Persons getPerson() {
        return person;
    }

    public void setPerson(Persons person) {
        this.person = person;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
