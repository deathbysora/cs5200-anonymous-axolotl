package beerapp.dal;

import BeerApp.model.Brewers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BrewersDao {
  private ConnectionManager  connectionManager;
  private static BrewersDao instance = null;
  private BrewersDao() {
    connectionManager = new ConnectionManager();
  }

  public static BrewersDao getInstance() {
    if(instance == null){
        instance = new BrewersDao();
    }
    return instance;
}

  public Brewers create(Brewers brewer) throws SQLException {
    String insertBrewer = "INSERT INTO Brewers(BrewerId) VALUES(?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    try {
        connection = connectionManager.getConnection();
        insertStmt = connection.prepareStatement(insertBrewer);
        insertStmt.setInt(1, brewer.getBrewerId());
        insertStmt.executeUpdate();
        return brewer;
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    } finally {
        if(connection != null) {
            connection.close();
        }
        if(insertStmt != null) {
            insertStmt.close();
        }
    }
  }

  public Brewers getBrewerById(int brewerId) throws SQLException {
    String selectBrewer = "SELECT BrewerId FROM Brewers WHERE BrewerId=?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
        connection = connectionManager.getConnection();
        selectStmt = connection.prepareStatement(selectBrewer);
        selectStmt.setInt(1, brewerId);
        results = selectStmt.executeQuery();
        if (results.next()) {
          int resultBrewerId = results.getInt("BrewerId");
          return new Brewers(resultBrewerId);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    } finally {
        if(connection != null) {
            connection.close();
        }
        if(selectStmt != null) {
            selectStmt.close();
        }
        if(results != null) {
            results.close();
        }
    }
    return null;
}

  public Brewers updateBrewerId(Brewers brewer, int newBrewerId)
    throws SQLException {
    String updateBrewer = "UPDATE Brewers SET BrewerId=? WHERE BrewerId=?;";
    Connection connection = null;
    PreparedStatement updateStmt = null;
    try {
        connection = connectionManager.getConnection();
        updateStmt = connection.prepareStatement(updateBrewer);
        updateStmt.setInt(1, newBrewerId);
        updateStmt.setInt(2, brewer.getBrewerId());
        updateStmt.executeUpdate();
        brewer.setBrewerId(newBrewerId);
        return brewer;
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    } finally {
        if(connection != null) {
            connection.close();
        }
        if(updateStmt != null) {
            updateStmt.close();
        }
    }
  }

  public Brewers delete(Brewers brewer) throws SQLException {
    String deleteBrewer = "DELETE FROM Brewers WHERE BrewerId=?;";
    Connection connection = null;
    PreparedStatement deleteStmt = null;
    try {
        connection = connectionManager.getConnection();
        deleteStmt = connection.prepareStatement(deleteBrewer);
        deleteStmt.setInt(1, brewer.getBrewerId());
        deleteStmt.executeUpdate();
        return null;
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    } finally {
        if(connection != null) {
            connection.close();
        }
        if(deleteStmt != null) {
            deleteStmt.close();
        }
    }
}
}