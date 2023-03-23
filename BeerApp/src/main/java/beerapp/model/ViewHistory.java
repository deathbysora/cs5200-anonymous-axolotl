package beerapp.model;

import java.sql.Date;

public class ViewHistory {
    protected int viewId;
    protected Date created;
    protected Person person;
    protected Beer beer;
    
    public ViewHistory(Date created, Person person, Beer beer) {
        this.created = created;
        this.person = person;
        this.beer = beer;
    }

    public ViewHistory(int viewId, Date created, Person person, Beer beer) {
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
