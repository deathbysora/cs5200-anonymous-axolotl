package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.Beer;
import beerapp.model.BeerReview;
import beerapp.model.BeerStyle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeersDao {

    private final static String TABLE_NAME = "beers";
    private static BeersDao instance;
    protected ConnectionManager connectionManager;

    private BeersDao() {
        connectionManager = new ConnectionManager();
    }

    public static BeersDao getInstance() {
        if (instance == null) {
            instance = new BeersDao();
        }
        return instance;
    }

    private static void prepareStatement(PreparedStatement statement, Beer beer) {
        try {
            statement.setInt(1, beer.getId());
            statement.setString(2, beer.getName());
            statement.setFloat(3, beer.getAbv());
            statement.setInt(4, beer.getBrewerId());
            statement.setString(5, beer.getBeerStyle().getStyle());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Beer deserializeResult(ResultSet result) {
        try {
            return new Beer(
              result.getInt("beerid"),
              result.getString("beername"),
              result.getFloat("abv"),
              result.getInt("brewerid"),
              new BeerStyle(result.getString("style"))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Beer createBeer(Beer beer) {
        String query = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?);";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement statement = connection.prepareStatement(query)
        ) {
            prepareStatement(statement, beer);
            statement.executeUpdate();
            return beer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Beer getBeerById(Integer beerId) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE beerid=?;";
        ResultSet result = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, beerId);
            result = statement.executeQuery();
            if (result.next()) {
                return deserializeResult(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(result);
        }
        return null;
    }

    public List<Beer> getBeersByStyle(BeerStyle style) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE style=?;";
        List<Beer> beers = new ArrayList<>();
        ResultSet result = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, style.getStyle());
            result = statement.executeQuery();
            while (result.next()) {
                beers.add(deserializeResult(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(result);
        }
        return beers;
    }

    public List<Beer> getBeersByCompanyId(Integer companyId) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE brewerid=?;";
        List<Beer> beers = new ArrayList<>();
        ResultSet result = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, companyId);
            result = statement.executeQuery();
            while (result.next()) {
                beers.add(deserializeResult(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(result);
        }
        return beers;
    }

    public Beer deleteBeer(Beer beer) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE beerid=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, beer.getId());
            if (statement.executeUpdate() == 0) {
                throw new RuntimeException("beer id: " + beer.getId() + " does not exist.");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * User can get a list of similar beer recommendations based on the beer
     * they selected. 
     * 
     * @param beer the beer object that user selected
     * @return a list of beers (could be just one or null) which the metrics are similar to the
     * recommended beer.
     */
    public List<BeerReview> getSimilarBeers(Beer beer) throws SQLException {

        List<BeerReview> resultSet = new ArrayList<>();
        ResultSet results = null;
        //BeerReviewsDao beerReviewsDao = BeerReviewsDao.getInstance();

        String beersLookupSQL = 
        "SELECT * FROM " +
        "(" +
            "SELECT BeerReview.BeerID as ID, AVG(BeerReview.Appearance) as Appearance, AVG(BeerReview.Aroma) as Aroma," +
            "    AVG(BeerReview.Palate) as Palate, AVG(BeerReview.Taste) as Taste, AVG(BeerReview.Overall) as Overall" +
            "FROM BeerReview " +
            "WHERE BeerReview.BeerID = ? " +
            "GROUP BY ID, Appearance, Aroma, Palate, Taste, Overall" +
        ") AS T INNER JOIN BeerReview ON T.BeerId = BeerReview.BeerId" +
        "WHERE (" + 
            "ABS(T.Appearance - BeerReview.Appearance) <=1 AND ABS(T.Aroma - BeerReview.Aroma) <= 1 " +
            "AND ABS(T.Palate - BeerReview.Palate)<=1 AND ABS(T.Taste - BeerReview.Taste)<= 1 " +
            "AND ABS(T.Overall - BeerReview.Overall)<=1" +
        ") LIMIT 5;";

        try (
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(beersLookupSQL);
        ) {
            statement.setInt(1, beer.getId());
            results = statement.executeQuery();
            while (results.next()) {
                resultSet.add(BeerReviewsDao.deserializeReview(results));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
        return resultSet;
    }
}