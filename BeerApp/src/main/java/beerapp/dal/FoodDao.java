package beerapp.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FoodDao {
	protected ConnectionManager connectionManager;
	
	private static FoodDao instance = null;
	protected FoodDao() {
		connectionManager = new ConnectionManager();
	}
	public static FoodDao getInstance() {
		if(instance == null) {
			instance = new FoodDao();
		}
		return instance;
	}
	
	public Food create(Food food) throws SQLException {
        String insertFood =
                "INSERT INTO Food(FoodName, Style)" + 
                "VALUEs(?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
        	connection = connectionManager.getConnection();
        	insertStmt = connection.prepareStatement(insertFood);
        	insertStmt.setString(1, food.getFoodName());
        	insertStmt.setString(2, food.getStyle().getStyle());
        	insertStmt.executeUpdate();
        	
        	return food;
        } catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (insertStmt != null) {
				connection.close();
			}
		}
    }
    
    public Food updateFoodName(Food food, String newFoodName) throws SQLException {
    	String updateFoodName = 
    			"UPDATE Food SET FoodName =? where FoodName =?;";
    	Connection connection = null;
    	PreparedStatement updateStmt = null;
    	try {
    		connection = connectionManager.getConnection();
    		updateStmt = connection.prepareStatement(updateFoodName);
    		updateStmt.setString(1, newFoodName);
    		updateStmt.setString(2, food.getFoodName());
    		updateStmt.executeUpdate();
    		
    		food.setFoodName(newFoodName);
    		return food;
    	} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (updateStmt != null) {
				connection.close();
			}
		}
    }
    
    public Food delete(Food food) throws SQLException {
    	String deleteFood = "DELETE FROM Food WHERE FoodName =?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteFood);
			deleteStmt.setString(1, food.getFoodName());
			deleteStmt.executeUpdate();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (deleteStmt != null) {
				connection.close();
			}
		}
    }
    
    public List<Food> getFoodByStyle(BeerStyles style) throws SQLException{
    	List<Food> foods = new ArrayList<Food>();
    	String selectFood = 
				"SELECT FoodName, Style" + 
				"FROM Food WHERE Style =?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectFood);
			selectStmt.setString(1, style.getStyle());
			
			results = selectStmt.executeQuery();
			
			while (results.next()) {
				String foodName = results.getString("FoodName");
				Food newFood = new Food(foodName, style);
				foods.add(newFood);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (selectStmt != null) {
				connection.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return foods;
    }
}