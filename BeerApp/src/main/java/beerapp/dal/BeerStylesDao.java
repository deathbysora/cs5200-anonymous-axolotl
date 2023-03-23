package beerapp.dal;

import beerapp.model.BeerStyle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BeerStylesDao {
    private final static String TABLE_NAME = "beerstyles";

    private static BeerStylesDao instance = null;
    protected ConnectionManager connectionManager;

    protected BeerStylesDao() {
        connectionManager = new ConnectionManager();
    }

    public static BeerStylesDao getInstance() {
        if (instance == null) {
            instance = new BeerStylesDao();
        }
        return instance;
    }

    public BeerStyle create(BeerStyle beerStyle) {
        String insertBeerStyle =
          "INSERT INTO " + TABLE_NAME + " (Style) VALUES(?);";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement insertStmt = connection.prepareStatement(insertBeerStyle)
        ) {
            insertStmt.setString(1, beerStyle.getStyle());
            insertStmt.executeUpdate();
            return beerStyle;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BeerStyle updateStyle(BeerStyle beerStyle, String newStyle) {
        String updateBeerStyle =
          "UPDATE " + TABLE_NAME + " SET Style=? where Style=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement updateStmt = connection.prepareStatement(updateBeerStyle)
        ) {
            updateStmt.setString(1, newStyle);
            updateStmt.setString(2, beerStyle.getStyle());
            updateStmt.executeUpdate();
            return new BeerStyle(newStyle);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BeerStyle delete(BeerStyle beerStyle) {
        String deleteBeerStyle = "DELETE FROM " + TABLE_NAME + " WHERE Style =?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement deleteStmt = connection.prepareStatement(deleteBeerStyle);
        ) {
            deleteStmt.setString(1, beerStyle.getStyle());
            deleteStmt.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BeerStyle getBeerStyle(String style) {
        String selectBeerStyle =
          "SELECT Style FROM " + TABLE_NAME + " WHERE Style =?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement selectStmt = connection.prepareStatement(selectBeerStyle)
        ) {
            selectStmt.setString(1, style);

            results = selectStmt.executeQuery();
            if (results.next()) {
                String resultStyle = results.getString("Style");
                return new BeerStyle(resultStyle);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Utility.safeCloseResultSet(results);
        }
        return null;
    }
}