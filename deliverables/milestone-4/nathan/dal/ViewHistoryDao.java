package BeerApp.dal;

import BeerApp.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewHistory {
    protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static ViewHistoryDao instance = null;
	protected ViewHistoryDao() {
		connectionManager = new ConnectionManager();
	}
	public static ViewHistoryDao getInstance() {
		if(instance == null) {
			instance = new ViewHistoryDao();
		}
		return instance;
	}

    public ViewHistory create(ViewHistory viewHistory) throws SQLException {
        String insertViewHistory = "INSERT INTO ViewHistory(Created,UserName,BeerId) VALUES(?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
        try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertViewHistory,
				Statement.RETURN_GENERATED_KEYS);
			insertStmt.setTimestamp(1, new Timestamp(viewHistory.getCreated().getTime()));
			insertStmt.setInt(2, viewHistory.getBeer().getBeerId());
			insertStmt.setString(3, viewHistory.getPerson().getUserName());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int viewId = -1;
			if(resultKey.next()) {
				viewId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			viewHistory.setCommentId(viewId);
			return viewHistory;
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
            if(resultKey != null) {
                resultKey.close();
            }
        }
    }

    public ViewHistory delete(ViewHistory viewHistory) throws SQLException {
		String deleteViewHistory = "DELETE FROM ViewHistory WHERE ViewId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteViewHistory);
			deleteStmt.setInt(1, viewHistory.getViewId());
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

    public ViewHistory getViewHistoryByViewId(int viewId) {
        String selectViewHistory = "SELECT ViewId,Created,UserName,BeerId FROM ViewHistory WHERE ViewId=?;";
        Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
        try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectViewHistory);
			selectStmt.setInt(1, viewId);
			results = selectStmt.executeQuery();
            BeerDao beerDao = BeerDao.getInstance();
            PersonDao personDao = PersonDao.getInstance();
			if(results.next()) {
				int resultViewId = results.getInt("ViewId");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int beerId = results.getInt("BeerId");
                String userName = results.getString("UserName");

                Beer beer = new Beer(beerId);
				Persons person = new Persons(userName);
                ViewHistory viewHistory = new ViewHistory(resultViewId, created, beer, person);
				return viewHistory;
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
    }

    public List<ViewHistory> getViewHistoryArrayForPerson(Person person) {
        List<ViewHistory> viewHistories = new ArrayList<ViewHistory>();
        String selectViewHistory = "SELECT ViewId,Created,UserName,BeerId FROM ViewHistory WHERE UserName=?;";
        Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
        try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectBeerComment);
			selectStmt.setString(1, person.getUserName());
			results = selectStmt.executeQuery();
            BeerDao beerDao = BeerDao.getInstance();
			if(results.next()) {
				int viewId = results.getInt("ViewId");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int beerId = results.getInt("BeerId");

                Beer beer = new Beer(beerId);
                ViewHistory viewHistory = new ViewHistory(viewId, created, beer, person);
                viewHistories.add(viewHistory);
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
		return viewHistories;
    }

    public List<ViewHistory> getViewHistoryArrayForBeer(Beer beer) {
        List<ViewHistory> viewHistories = new ArrayList<ViewHistory>();
        String selectViewHistory = "SELECT ViewId,Created,UserName,BeerId FROM ViewHistory WHERE BeerId=?;";
        Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
        try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectBeerComment);
			selectStmt.setString(1, beer.getBeerId());
			results = selectStmt.executeQuery();
            PersonsDao personDao = PersonsDao.getInstance();
			if(results.next()) {
				int viewId = results.getInt("ViewId");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int beerId = results.getInt("BeerId");
                String userName = results.getString("UserName");

                Persons person = personsDao.getPersonFromUserName(userName);
                ViewHistory viewHistory = new ViewHistory(viewId, created, beer, person);
                viewHistories.add(viewHistory);
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
		return viewHistories;
    }
}
