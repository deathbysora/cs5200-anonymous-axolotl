package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.Brewer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BrewersDao {

    private final static String TABLE_NAME = "brewers";
    private static BrewersDao instance = null;
    private final ConnectionManager connectionManager;

    private BrewersDao() {
        connectionManager = new ConnectionManager();
    }

    public static BrewersDao getInstance() {
        if (instance == null) {
            instance = new BrewersDao();
        }
        return instance;
    }

    public Brewer create(Brewer brewer) {
        String insertBrewer = "INSERT INTO " + TABLE_NAME + " (BrewerId) VALUES(?);";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement insertStmt = connection.prepareStatement(insertBrewer)
        ) {
            insertStmt.setInt(1, brewer.getBrewerId());
            insertStmt.executeUpdate();
            return brewer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Brewer getBrewerById(int brewerId) {
        String selectBrewer = "SELECT BrewerId FROM " + TABLE_NAME + " WHERE BrewerId=?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement selectStmt = connection.prepareStatement(selectBrewer)
        ) {
            selectStmt.setInt(1, brewerId);
            results = selectStmt.executeQuery();
            if (results.next()) {
                int resultBrewerId = results.getInt("BrewerId");
                return new Brewer(resultBrewerId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
        return null;
    }

    public Brewer updateBrewerId(Brewer brewer, int newBrewerId) {
        String updateBrewer = "UPDATE " + TABLE_NAME + " SET BrewerId=? WHERE BrewerId=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement updateStmt = connection.prepareStatement(updateBrewer)
        ) {
            updateStmt.setInt(1, newBrewerId);
            updateStmt.setInt(2, brewer.getBrewerId());
            updateStmt.executeUpdate();
            return new Brewer(newBrewerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Brewer delete(Brewer brewer) {
        String deleteBrewer = "DELETE FROM " + TABLE_NAME + " WHERE BrewerId=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement deleteStmt = connection.prepareStatement(deleteBrewer)
        ) {
            deleteStmt.setInt(1, brewer.getBrewerId());
            deleteStmt.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}