package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.BeerReview;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BeerReviewsDao {

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

    private static BeerReview deserializeReview(ResultSet result) {
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
          "INSERT INTO BeerReviews(Appearance, Aroma, Palate, Taste," +
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
        String lookUpSQL = "SELECT * FROM BeerReviews WHERE ReviewId=?;";
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
        String lookUpSQL = "SELECT * FROM BeerReviews WHERE UserName=?;";
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
        String lookUpSQL = "SELECT * FROM BeerReviews WHERE RestaurantId=?;";
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
        String deleteSQL = "DELETE FROM BeerReviews WHERE ReviewID=?";
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
}