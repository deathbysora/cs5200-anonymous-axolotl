package beerapp.dal;

import beerapp.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeerStylesDao {
	protected ConnectionManager connectionManager;

    private static BeerStylesDao instance = null;
    protected BeerStylesDao() {
        connectionManager = new ConnectionManager();
    }
    public static BeerStylesDao getInstance() {
        if(instance == null) {
            instance = new BeerStylesDao();
        }
        return instance;
    }

    public BeerStyles create(BeerStyles beerStyle) throws SQLException {
        String insertBeerStyles =
                "INSERT INTO BeerStyles(Style)" + 
                "VALUEs(?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
        	connection = connectionManager.getConnection();
        	insertStmt = connection.prepareStatement(insertBeerStyles);
        	insertStmt.setString(1, beerStyle.getStyle());
        	insertStmt.executeUpdate();
        	
        	return beerStyle;
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
    
    public BeerStyles updateStyle(BeerStyles beerStyle, String style) throws SQLException {
    	String updateBeerStyle = 
    			"UPDATE BeerStyles SET Style =? where Style =?;";
    	Connection connection = null;
    	PreparedStatement updateStmt = null;
    	try {
    		connection = connectionManager.getConnection();
    		updateStmt = connection.prepareStatement(updateBeerStyle);
    		updateStmt.setString(1, style);
    		updateStmt.setString(2, beerStyle.getStyle());
    		updateStmt.executeUpdate();
    		
    		beerStyle.setStyle(style);
    		return beerStyle;
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
    
    public BeerStyles delete(BeerStyles beerStyle) throws SQLException {
    	String deleteBeerStyles = "DELETE FROM BeerStyles WHERE Style =?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteBeerStyles);
			deleteStmt.setString(1, beerStyle.getStyle());
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
    
    public BeerStyles getBeerStyleByStyle(String style) throws SQLException {
    	String selectBeerStyles = 
				"SELECT Style" + 
				"FROM BeerStyles WHERE Style =?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectBeerStyles);
			selectStmt.setString(1, style);
			
			results = selectStmt.executeQuery();
			if (results.next()) {
				String resultStyle = results.getString("Style");
			
				BeerStyles beerStyle = new BeerStyles(resultStyle);
				return beerStyle;
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
		return null;
    }
}