package beerapp.model;

import java.sql.Date;

/**
 * This is the object for the BeerReview in the Java interface (JDBC)
 */
public class BeerReview {
  // fields
  protected int reviewId;
  protected float appearance;
  protected float aroma;
  protected float palate;
  protected float taste;
  protected float overall;
  protected Date created;
  protected String text;
  protected User user;
  protected Beer beer;
  
  // constructors
  public BeerReview(int reviewId, float appearance, float aroma, float palate, float taste,
                     float overall, Date created, String text, User user, Beer beer) {
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
  
  public BeerReview(float appearance, float aroma, float palate, float taste,
                     float overall, Date created, String text, User user, Beer beer) {
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
  
  // getters and setters
  
  public int getReviewId() {
    return reviewId;
  }
  
  public void setReviewId(int reviewId) {
    this.reviewId = reviewId;
  }
  
  public float getAppearance() {
    return appearance;
  }
  
  public void setAppearance(float appearance) {
    this.appearance = appearance;
  }
  
  public float getAroma() {
    return aroma;
  }
  
  public void setAroma(float aroma) {
    this.aroma = aroma;
  }
  
  public float getPalate() {
    return palate;
  }
  
  public void setPalate(float palate) {
    this.palate = palate;
  }
  
  public float getTaste() {
    return taste;
  }
  
  public void setTaste(float taste) {
    this.taste = taste;
  }
  
  public float getOverall() {
    return overall;
  }
  
  public void setOverall(float overall) {
    this.overall = overall;
  }
  
  public Date getCreated() {
    return created;
  }
  
  public void setCreated(Date created) {
    this.created = created;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public Beer getBeer() {
    return beer;
  }
  
  public void setBeer(Beer beer) {
    this.beer = beer;
  }
}