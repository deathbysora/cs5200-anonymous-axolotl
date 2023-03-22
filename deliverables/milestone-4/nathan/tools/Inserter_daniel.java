package restaurant.tools;

import restaurant.dal.*;
import restaurant.model.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Inserter_daniel {
  
  public static void main(String[] args) throws SQLException {
    // DAO instances
    BeerReviewDao reviewsDao = BeerReviewDao.getInstance();
    
    // Insert object from models
    BeerReviews review1 = new BeerReviews();
    review1 = reviewsDao.create(review1);
  
    BeerReviews review11 = reviewsDao.getReviewById(1);
    List<BeerReviews> reviews111 = reviewsDao.getReviewsByUserName("dbi");
    List<BeerReviews> reviews112 = reviewsDao.getReviewsByBeerId(2);
    review11 = reviewsDao.delete(review11);
    for (BeerReviews p : reviews111) {
      System.out.println(p);
    }
    for (BeerReviews p : reviews112) {
      System.out.println(p);
    }
  }
}
