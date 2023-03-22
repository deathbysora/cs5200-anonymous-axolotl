package restaurant.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import restaurant.model.BeerReviews;
import restaurant.model.Restaurants;
import restaurant.model.Reviews;
import restaurant.model.Users;

public class BeerReviewDao {
  // Set up connections
  private static BeerReviewDao instance = null;
  protected ConnectionManager connectionManager;
  
  protected BeerReviewDao() {
    connectionManager = new ConnectionManager();
  }
  
  public static BeerReviewDao getInstance() {
    if (instance == null) {
      instance = new BeerReviewDao();
    }
    return instance;
  }
  
  /**
   * Create a beer review
   *
   * @param review a review object
   * @return the same review object
   * @throws SQLException
   */
  public BeerReviews create(BeerReviews review) throws SQLException {
    // initialize the sql statement
    String createReviewSQL =
            "INSERT INTO BeerReviews(Appearance, Aroma, Palate, Taste," +
                    " Overall, Created, Text, UserName, BeerID) VALUES(?,?,?,?,?,?,?,?,?)";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    ResultSet resultKey = null;
    
    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(createReviewSQL, Statement.RETURN_GENERATED_KEYS);
      insertStmt.setFloat(1, review.getAppearance());
      insertStmt.setFloat(2, review.getAroma());
      insertStmt.setFloat(3, review.getPalate());
      insertStmt.setFloat(4, review.getTaste());
      insertStmt.setFloat(5, review.getOverall());
      insertStmt.setDate(6, review.getCreated());
      insertStmt.setString(7, review.getText());
      insertStmt.setString(8, review.getUser().getUserName());
      insertStmt.setInt(9, review.getBeer().getBeerId());
      insertStmt.executeUpdate();
      
      // retrieve the key
      resultKey = insertStmt.getGeneratedKeys();
      int recID = -1;
      if (resultKey.next()) {
        recID = resultKey.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key.");
      }
      review.setReviewId(recID);
      return review;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
      if (resultKey != null) {
        resultKey.close();
      }
    }
  }
  
  /**
   * Look up a review by its ID
   *
   * @param reviewId
   * @return
   * @throws SQLException
   */
  public BeerReviews getReviewById(int reviewId) throws SQLException {
    // initialize the sql statement
    String lookUpSQL =
            "SELECT * FROM BeerReviews WHERE ReviewId=?";
    Connection connection = null;
    PreparedStatement lookUpStatement = null;
    ResultSet results = null;
  
    try {
      connection = connectionManager.getConnection();
      lookUpStatement = connection.prepareStatement(lookUpSQL);
      lookUpStatement.setInt(1, reviewId);
      results = lookUpStatement.executeQuery();
    
      UsersDao userDao = UsersDao.getInstance();
      RestaurantDao restDao = RestaurantDao.getInstance();
      // analyze results
      if (results.next()) {
        int reviewIdResult = results.getInt("ReviewId");
        float appearance = results.getFloat("Appearance");
        float aroma = results.getFloat("Aroma");
        float palate = results.getFloat("Palate");
        float taste = results.getFloat("Taste");
        float overall = results.getFloat("Overall");
        Date created = results.getDate("Created");
        String text = results.getString("Text");
        String userName = results.getString("UserName");
        int beerId = results.getInt("BeerId");
        // construct other user and restaurant object for the recommendation object
        // TO-DO: not working
        Users user = UsersDao.getUserFromUserName(userName);
        Beers beer = BeersDao.getBeerId(beerId);
        // construct and return the object
        BeerReviews review = new BeerReviews(reviewIdResult, appearance, aroma, palate, taste,
                overall, created, text, user, beer);
        return review;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (lookUpStatement != null) {
        lookUpStatement.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return null;
  }
  
  /**
   * Look up review(s) by a specific user.
   * @param userName
   * @return
   * @throws SQLException
   */
  public List<BeerReviews> getReviewsByUserName(String userName) throws SQLException {
    // initialize the return list
    List<BeerReviews> resultsReview = new ArrayList<>();
    // initialize the sql statement
    String lookUpSQL =
            "SELECT * FROM BeerReviews WHERE UserName=?";
    Connection connection = null;
    PreparedStatement lookUpStatement = null;
    ResultSet results = null;
    
    try {
      connection = connectionManager.getConnection();
      lookUpStatement = connection.prepareStatement(lookUpSQL);
      lookUpStatement.setString(1, userName);
      results = lookUpStatement.executeQuery();
      
      UsersDao userDao = UsersDao.getInstance();
      RestaurantDao restDao = RestaurantDao.getInstance();
      // analyze results
      while (results.next()) {
        int reviewIdResult = results.getInt("ReviewId");
        float appearance = results.getFloat("Appearance");
        float aroma = results.getFloat("Aroma");
        float palate = results.getFloat("Palate");
        float taste = results.getFloat("Taste");
        float overall = results.getFloat("Overall");
        Date created = results.getDate("Created");
        String text = results.getString("Text");
        String userNameResult = results.getString("UserName");
        int beerId = results.getInt("BeerId");
        // construct other user and restaurant object for the recommendation object
        // TO-DO: not working
        Users user = UsersDao.getUserFromUserName(userNameResult);
        Beers beer = BeersDao.getBeerId(beerId);
        // construct and return the object
        BeerReviews review = new BeerReviews(reviewIdResult, appearance, aroma, palate, taste,
                overall, created, text, user, beer);
        //return review;
        resultsReview.add(review);
      }
      return resultsReview;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (lookUpStatement != null) {
        lookUpStatement.close();
      }
      if (results != null) {
        results.close();
      }
    }
  }
  
  /**
   * Get a list of reviews from a specific restaurant
   * @param restaurantId
   * @return
   * @throws SQLException
   */
  public List<BeerReviews> getReviewsByBeerId(int BeerId) throws SQLException {
    // initialize the return list
    List<BeerReviews> resultsReview = new ArrayList<>();
    // initialize the sql statement
    String lookUpSQL =
            "SELECT * FROM BeerReviews WHERE RestaurantId=?";
    Connection connection = null;
    PreparedStatement lookUpStatement = null;
    ResultSet results = null;
    
    try {
      connection = connectionManager.getConnection();
      lookUpStatement = connection.prepareStatement(lookUpSQL);
      lookUpStatement.setInt(1, BeerId);
      results = lookUpStatement.executeQuery();
      
      UsersDao userDao = UsersDao.getInstance();
      RestaurantDao restDao = RestaurantDao.getInstance();
      // analyze results
      while (results.next()) {
        int reviewIdResult = results.getInt("ReviewId");
        float appearance = results.getFloat("Appearance");
        float aroma = results.getFloat("Aroma");
        float palate = results.getFloat("Palate");
        float taste = results.getFloat("Taste");
        float overall = results.getFloat("Overall");
        Date created = results.getDate("Created");
        String text = results.getString("Text");
        String userNameResult = results.getString("UserName");
        int beerId = results.getInt("BeerId");
        // construct other user and restaurant object for the recommendation object
        // TO-DO: not working
        Users user = UsersDao.getUserFromUserName(userNameResult);
        Beers beer = BeersDao.getBeerId(beerId);
        // construct and return the object
        BeerReviews review = new BeerReviews(reviewIdResult, appearance, aroma, palate, taste,
                overall, created, text, user, beer);
        //return review;
        resultsReview.add(review);
      }
      return resultsReview;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (lookUpStatement != null) {
        lookUpStatement.close();
      }
      if (results != null) {
        results.close();
      }
    }
  }
  
  /**
   * Delete a review
   * @param review the recommendation java object
   * @return NULL if successful deletion
   * @throws SQLException
   */
  public BeerReviews delete(BeerReviews review) throws SQLException {
    String deleteSQL = "DELETE FROM BeerReviews WHERE ReviewID=?";
    Connection connection = null;
    PreparedStatement deleteStatement = null;
    
    try {
      connection = connectionManager.getConnection();
      deleteStatement = connection.prepareStatement(deleteSQL);
      deleteStatement.setInt(1, review.getReviewId());
      deleteStatement.executeUpdate();
      
      // return null so the caller can no longer operate on the instance
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if(connection != null) {
        connection.close();
      }
      if(deleteStatement != null) {
        deleteStatement.close();
      }
    }
  }
  
  
}
