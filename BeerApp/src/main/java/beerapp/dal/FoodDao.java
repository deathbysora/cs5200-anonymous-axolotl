package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.BeerStyle;
import beerapp.model.Food;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodDao {

    private final static String TABLE_NAME = "food";
    private static FoodDao instance = null;
    protected ConnectionManager connectionManager;

    protected FoodDao() {
        connectionManager = new ConnectionManager();
    }

    public static FoodDao getInstance() {
        if (instance == null) {
            instance = new FoodDao();
        }
        return instance;
    }

    public Food create(Food food) {
        String insertFood = "INSERT INTO " + TABLE_NAME + " (FoodName, style) VALUES(?,?);";
        try (
          Connection connection = connectionManager.getConnection();
        PreparedStatement insertStmt = connection.prepareStatement(insertFood);
          ) {
            insertStmt.setString(1, food.getFoodName());
            insertStmt.setString(2, food.getStyle().getStyle());
            insertStmt.executeUpdate();

            return food;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Food updateFoodName(Food food, String newFoodName) {
        String updateFoodName = "UPDATE " + TABLE_NAME + " SET FoodName=? where FoodName=?;";
        try (
          Connection connection = connectionManager.getConnection();
        PreparedStatement updateStmt = connection.prepareStatement(updateFoodName);
          ) {
            updateStmt.setString(1, newFoodName);
            updateStmt.setString(2, food.getFoodName());
            updateStmt.executeUpdate();

            food.setFoodName(newFoodName);
            return food;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Food delete(Food food) {
        String deleteFood = "DELETE FROM " + TABLE_NAME + " WHERE FoodName =?;";
        try (
          Connection connection = connectionManager.getConnection();
        PreparedStatement deleteStmt = connection.prepareStatement(deleteFood);
          ) {
            deleteStmt.setString(1, food.getFoodName());
            deleteStmt.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Food> getFoodByName(String foodName) {
        List<Food> foods = new ArrayList<>();
        String selectFood = "SELECT * FROM " + TABLE_NAME + " WHERE foodname=?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement selectStmt = connection.prepareStatement(selectFood);
        ) {
            selectStmt.setString(1, foodName);
            results = selectStmt.executeQuery();
            while (results.next()) {
                foods.add(new Food(
                  results.getString("foodname"),
                  new BeerStyle(results.getString("style"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
        return foods;
    }

    public List<Food> getFoodByStyle(BeerStyle style) {
        List<Food> foods = new ArrayList<>();
        String selectFood =
          "SELECT FoodName, Style FROM " + TABLE_NAME + " WHERE Style=?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
        PreparedStatement selectStmt = connection.prepareStatement(selectFood);
          ) {
            selectStmt.setString(1, style.getStyle());
            results = selectStmt.executeQuery();

            while (results.next()) {
                String foodName = results.getString("FoodName");
                Food newFood = new Food(foodName, style);
                foods.add(newFood);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
        return foods;
    }
}