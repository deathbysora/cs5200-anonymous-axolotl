package beerapp.model;

import java.sql.Date;

public class BeerReview {

    // fields
    protected Integer reviewId;
    protected Float appearance;
    protected Float aroma;
    protected Float palate;
    protected Float taste;
    protected Float overall;
    protected Date created;
    protected String text;
    protected User user;
    protected Beer beer;

    // constructors
    public BeerReview(Integer reviewId, Float appearance, Float aroma, Float palate, Float taste,
      Float overall, Date created, String text, User user, Beer beer) {
        this.reviewId = reviewId;
        this.appearance = appearance;
        this.aroma = aroma;
        this.palate = palate;
        this.taste = taste;
        this.overall = overall;
        this.created = created;
        this.text = text;
        this.user = user;
        this.beer = beer;
    }

    public BeerReview(Float appearance, Float aroma, Float palate, Float taste,
      Float overall, Date created, String text, User user, Beer beer) {
        this.appearance = appearance;
        this.aroma = aroma;
        this.palate = palate;
        this.taste = taste;
        this.overall = overall;
        this.created = created;
        this.text = text;
        this.user = user;
        this.beer = beer;
    }

    public Integer getId() {
        return this.reviewId;
    }

    public Float getAppearance() {
        return this.appearance;
    }

    public Float getAroma() {
        return this.aroma;
    }

    public Float getPalate() {
        return this.palate;
    }

    public Float getTaste() {
        return this.taste;
    }

    public Float getOverall() {
        return this.overall;
    }

    public Date getCreated() {
        return this.created;
    }

    public String getText() {
        return this.text;
    }

    public User getUser() {
        return this.user;
    }

    public Beer getBeer() {
        return this.beer;
    }

    @Override
    public String toString() {
        return "BeerReview{" +
          "reviewId=" + reviewId +
          ", appearance=" + appearance +
          ", aroma=" + aroma +
          ", palate=" + palate +
          ", taste=" + taste +
          ", overall=" + overall +
          ", created=" + created +
          ", text='" + text + '\'' +
          ", user=" + user +
          ", beer=" + beer +
          '}';
    }
}