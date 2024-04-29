package beerapp.model;

import java.util.Date;

public class BeerComment {
    protected int commentId;
    protected String content;
    protected Date created;
    protected BeerReview beerReview;
    protected Person person;
    
    public BeerComment(String content, Date created, BeerReview beerReview, Person person) {
        this.content = content;
        this.created = created;
        this.beerReview = beerReview;
        this.person = person;
    }

    public BeerComment(int commentId, String content, Date created, BeerReview beerReview, Person person) {
        this.commentId = commentId;
        this.content = content;
        this.created = created;
        this.beerReview = beerReview;
        this.person = person;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public BeerReview getBeerReview() {
        return beerReview;
    }

    public void setBeerReview(BeerReview beerReview) {
        this.beerReview= beerReview;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    
}