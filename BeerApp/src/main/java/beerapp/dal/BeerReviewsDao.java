package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.Beer;
import beerapp.model.BeerReview;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BeerReviewsDao {

    private final static String TABLE_NAME = "BeerReviews";
    // Set up connections
    private static BeerReviewsDao instance = null;
    protected ConnectionManager connectionManager;

    protected BeerReviewsDao() {
        connectionManager = new ConnectionManager();
    }

    public static BeerReviewsDao getInstance() {
        if (instance == null) {
            instance = new BeerReviewsDao();
        }
        return instance;
    }

    public static BeerReview deserializeReview(ResultSet result) {
        UsersDao usersDao = UsersDao.getInstance();
        BeersDao beersDao = BeersDao.getInstance();
        try {
            return new BeerReview(
              result.getInt("ReviewId"),
              result.getFloat("Appearance"),
              result.getFloat("Aroma"),
              result.getFloat("Palate"),
              result.getFloat("Taste"),
              result.getFloat("Overall"),
              result.getDate("Created"),
              result.getString("Text"),
              usersDao.getUserFromUserName(result.getString("UserName")),
              beersDao.getBeerById(result.getInt("BeerId"))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a beer review
     *
     * @param review a review object
     * @return the same review object
     * @throws SQLException
     */
    public BeerReview create(BeerReview review) {
        // initialize the sql statement
        String createReviewSQL =
          "INSERT INTO " + TABLE_NAME + " (Appearance, Aroma, Palate, Taste," +
            " Overall, Created, Text, UserName, BeerID) VALUES(?,?,?,?,?,?,?,?,?);";
        ResultSet resultKey = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement insertStmt = connection.prepareStatement(createReviewSQL,
            Statement.RETURN_GENERATED_KEYS)
        ) {
            insertStmt.setFloat(1, review.getAppearance());
            insertStmt.setFloat(2, review.getAroma());
            insertStmt.setFloat(3, review.getPalate());
            insertStmt.setFloat(4, review.getTaste());
            insertStmt.setFloat(5, review.getOverall());
            insertStmt.setDate(6, review.getCreated());
            insertStmt.setString(7, review.getText());
            insertStmt.setString(8, review.getUser().getUsername());
            insertStmt.setInt(9, review.getBeer().getId());
            insertStmt.executeUpdate();

            // retrieve the key
            resultKey = insertStmt.getGeneratedKeys();
            int recID = -1;
            if (resultKey.next()) {
                recID = resultKey.getInt(1);
            } else {
                throw new RuntimeException("Unable to retrieve auto-generated key.");
            }
            return new BeerReview(
              recID,
              review.getAppearance(),
              review.getAroma(),
              review.getPalate(),
              review.getTaste(),
              review.getOverall(),
              review.getCreated(),
              review.getText(),
              review.getUser(),
              review.getBeer()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(resultKey);
        }
    }

    /**
     * Look up a review by its ID
     */
    public BeerReview getReviewById(int reviewId) {
        String lookUpSQL = "SELECT * FROM " + TABLE_NAME + " WHERE ReviewId=?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement lookUpStatement = connection.prepareStatement(lookUpSQL)
        ) {
            lookUpStatement.setInt(1, reviewId);
            results = lookUpStatement.executeQuery();
            if (results.next()) {
                return deserializeReview(results);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
        return null;
    }

    /**
     * Look up review(s) by a specific user.
     *
     * @param userName
     * @return
     * @throws SQLException
     */
    public List<BeerReview> getReviewsByUserName(String userName) {
        List<BeerReview> resultsReview = new ArrayList<>();
        String lookUpSQL = "SELECT * FROM " + TABLE_NAME + " WHERE UserName=?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement lookUpStatement = connection.prepareStatement(lookUpSQL)
        ) {
            lookUpStatement.setString(1, userName);
            results = lookUpStatement.executeQuery();

            while (results.next()) {
                resultsReview.add(deserializeReview(results));
            }
            return resultsReview;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
    }

    /**
     * Get a list of reviews from a specific restaurant
     */
    public List<BeerReview> getReviewsByBeerId(int beerId) {
        List<BeerReview> resultsReview = new ArrayList<>();
        String lookUpSQL = "SELECT * FROM " + TABLE_NAME + " WHERE RestaurantId=?;";
        ResultSet results = null;

        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement lookUpStatement = connection.prepareStatement(lookUpSQL)
        ) {
            lookUpStatement.setInt(1, beerId);
            results = lookUpStatement.executeQuery();
            while (results.next()) {
                resultsReview.add(deserializeReview(results));
            }
            return resultsReview;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
    }

    /**
     * Delete a review
     */
    public BeerReview delete(BeerReview review) {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE ReviewID=?";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL)
        ) {
            deleteStatement.setInt(1, review.getId());
            deleteStatement.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BeerReview findAverageReviewOf(Beer beer) {
        String query = "SELECT beerid, AVG(appearance) as appearance, AVG(aroma) as aroma, AVG(palate) as palate, AVG(taste) as taste " +
          "FROM " + TABLE_NAME + " WHERE beerid=? GROUP BY beerid;";
        ResultSet result = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement statement = connection.prepareStatement(query);
          ) {
            statement.setInt(1, beer.getId());
            result = statement.executeQuery();
            if (result.next()) {
                BeerReview review = new BeerReview(
                  null,
                  result.getFloat("appearance"),
                  result.getFloat("aroma"),
                  result.getFloat("palate"),
                  result.getFloat("taste"),
                  0f,
                  null,
                  null,
                  null,
                  null
                );
                return review;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(result);
        }
        return null;
    }

    public List<BeerReview> findSimilarReviews(BeerReview review, int limit) {
        List<BeerReview> reviews = new ArrayList<>();
        String query = "SELECT beerid, AVG(appearance) as appearance, AVG(aroma) as aroma, AVG(palate) as palate, AVG(taste) as taste " +
          "FROM " + TABLE_NAME + " " +
          "WHERE (" +
          "ABS(Appearance-?)<=1 AND ABS(Aroma-?)<=1 " +
          "AND ABS(Palate-?)<=1 AND ABS(Taste-?)<=1) " +
          "GROUP BY beerid " +
          "LIMIT ?;";
        ResultSet result = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setFloat(1, review.getAppearance());
            statement.setFloat(2, review.getAroma());
            statement.setFloat(3, review.getPalate());
            statement.setFloat(4, review.getTaste());
            statement.setInt(5, limit);
            result = statement.executeQuery();
            while (result.next()) {
                reviews.add(new BeerReview(
                  result.getInt("beerid"),
                  result.getFloat("appearance"),
                  result.getFloat("aroma"),
                  result.getFloat("palate"),
                  result.getFloat("taste"),
                  0f,
                  null,
                  null,
                  null,
                  null
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(result);
        }
        return reviews;
    }
    
    public List<String> getTopBeersCountsByIPA() {
        List<String> beerNames = new ArrayList<>();
        String selectBeers =
                "SELECT Beer.BeerName, COUNT(BeerReviews.ReviewId) AS review_count " +
                "FROM Beer " +
                "INNER JOIN BeerReviews ON Beer.BeerId = BeerReviews.BeerId " +
                "WHERE Beer.Style LIKE '%IPA%' " +
                "GROUP BY Beer.BeerId " +
                "ORDER BY review_count DESC " +
                "LIMIT 10;";

        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectBeers)) {
            resultSet = selectStmt.executeQuery();

            while (resultSet.next()) {
                beerNames.add(resultSet.getString("BeerName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(resultSet);
        }

        return beerNames;
    }


    public List<String> getTopBeersCountsByAle() {
        List<String> beerNames = new ArrayList<>();
        String selectBeers =
                "SELECT Beer.BeerName, COUNT(BeerReviews.ReviewId) AS review_count " +
                "FROM Beer " +
                "INNER JOIN BeerReviews ON Beer.BeerId = BeerReviews.BeerId " +
                "WHERE Beer.Style LIKE '%Ale%' " +
                "GROUP BY Beer.BeerId " +
                "ORDER BY review_count DESC " +
                "LIMIT 10;";

        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectBeers)) {
            resultSet = selectStmt.executeQuery();

            while (resultSet.next()) {
                beerNames.add(resultSet.getString("BeerName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(resultSet);
        }

        return beerNames;
    }


}