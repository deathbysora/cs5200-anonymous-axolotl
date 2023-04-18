package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.Beer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public enum BeersCountDao {
  INSTANCE;
  private ConnectionManager connectionManager;

  private BeersCountDao() {
    connectionManager = new ConnectionManager();
  }

  /**
     * Get a list of beerNames whose style contains "Lager" with the top ten number of reviews
     */
    public List<String> getTopBeersCountsByLager(int topNum) {
      List<String> resultsReview = new ArrayList<>();
      String lookUpSQL = "SELECT BeerName FROM " +
        "(SELECT b.beerId, b.BeerName FROM beers b WHERE b.Style LIKE '%Lager%') AS temp " +
        "INNER JOIN BeerReviews r ON temp.beerId = r.beerId " +
        "GROUP BY BeerName ORDER BY count(*) DESC LIMIT ?;";
      ResultSet results = null;

      try (
        Connection connection = connectionManager.getConnection();
        PreparedStatement lookUpStatement = connection.prepareStatement(lookUpSQL)
      ) {
          lookUpStatement.setInt(1, topNum);
          results = lookUpStatement.executeQuery();
          while (results.next()) {
            String beerNameRes = result.getString("BeerName");
            resultsReview.add(beerNameRes);
          }
          return resultsReview;
      } catch (SQLException e) {
          throw new RuntimeException(e);
      } finally {
          safeCloseResultSet(results);
      }
    }
}
