package beerapp.dal;

import beerapp.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ViewHistoryDao {
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
		ResultSet resultKey = null;
        try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement insertStmt = connection.prepareStatement(insertViewHistory,
				Statement.RETURN_GENERATED_KEYS);
		) {
			insertStmt.setTimestamp(1, new Timestamp(viewHistory.getCreated().getTime()));
			insertStmt.setInt(2, viewHistory.getBeer().getId());
			insertStmt.setString(3, viewHistory.getPerson().getUsername());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int viewId = -1;
			if(resultKey.next()) {
				viewId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			viewHistory.setViewId(viewId);
			return viewHistory;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
			if(resultKey != null) {
				resultKey.close();
			}
        }
    }

    public ViewHistory delete(ViewHistory viewHistory) throws SQLException {
		String deleteViewHistory = "DELETE FROM ViewHistory WHERE ViewId=?;";
		try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement deleteStmt = connection.prepareStatement(deleteViewHistory);
		) {
			deleteStmt.setInt(1, viewHistory.getViewId());
			deleteStmt.executeUpdate();

			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
		}
	}

    public ViewHistory getViewHistoryByViewId(int viewId) throws SQLException {
        String selectViewHistory = "SELECT ViewId,Created,UserName,BeerId FROM ViewHistory WHERE ViewId=?;";
        Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
        try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectViewHistory);
			selectStmt.setInt(1, viewId);
			results = selectStmt.executeQuery();
            BeersDao beerDao = BeersDao.getInstance();
            PersonsDao personDao = PersonsDao.getInstance();
			if(results.next()) {
				int resultViewId = results.getInt("ViewId");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int beerId = results.getInt("BeerId");
                String userName = results.getString("UserName");

                Beer beer = beerDao.getBeerById(beerId);
				Person person = personDao.getPersonByUsername(userName);
                ViewHistory viewHistory = new ViewHistory(resultViewId, created, person, beer);
				return viewHistory;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(results != null) {
				results.close();
			}
		}
		return null;
    }

    public List<ViewHistory> getViewHistoryArrayForPerson(Person person) throws SQLException {
        List<ViewHistory> viewHistories = new ArrayList<ViewHistory>();
        String selectViewHistory = "SELECT ViewId,Created,UserName,BeerId FROM ViewHistory WHERE UserName=?;";
		ResultSet results = null;
        try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement selectStmt = connection.prepareStatement(selectViewHistory);
		) {
			selectStmt.setString(1, person.getUsername());
			results = selectStmt.executeQuery();
            BeersDao beerDao = BeersDao.getInstance();
			if(results.next()) {
				int viewId = results.getInt("ViewId");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int beerId = results.getInt("BeerId");

                Beer beer = beerDao.getBeerById(beerId);
                ViewHistory viewHistory = new ViewHistory(viewId, created, person, beer);
                viewHistories.add(viewHistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(results != null) {
				results.close();
			}
		}
		return viewHistories;
    }

    public List<ViewHistory> getViewHistoryArrayForBeer(Beer beer) throws SQLException {
        List<ViewHistory> viewHistories = new ArrayList<ViewHistory>();
        String selectViewHistory = "SELECT ViewId,Created,UserName,BeerId FROM ViewHistory WHERE BeerId=?;";
		ResultSet results = null;
        try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement selectStmt = connection.prepareStatement(selectViewHistory);
		) {
			selectStmt.setInt(1, beer.getId());
			results = selectStmt.executeQuery();
            PersonsDao personDao = PersonsDao.getInstance();
			if(results.next()) {
				int viewId = results.getInt("ViewId");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int beerId = results.getInt("BeerId");
                String userName = results.getString("UserName");

                Person person = personDao.getPersonByUsername(userName);
                ViewHistory viewHistory = new ViewHistory(viewId, created, person, beer);
                viewHistories.add(viewHistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(results != null) {
				results.close();
			}
		}
		return viewHistories;
    }
}
