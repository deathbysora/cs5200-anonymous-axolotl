package BeerApp.dal;

import BeerApp.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Data access object (DAO) class to interact with the underlying Administrators table in your
 * MySQL instance. This is used to store {@link Administrators} into your MySQL instance and 
 * retrieve {@link Administrators} from MySQL instance.
 */
public class AdministratorsDao extends PersonsDao {
	private static AdministratorsDao instance = null;
	protected AdministratorsDao() {
		super();
	}
	public static AdministratorsDao getInstance() {
		if(instance == null) {
			instance = new AdministratorsDao();
		}
		return instance;
	}
	
	public Administrators create(Administrators administrator) throws SQLException {
		// Insert into the superclass table first.
		create(new Persons(administrator.getUserName()));

		String insertAdministrator = "INSERT INTO Administrators(UserName) VALUES(?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertAdministrator);
			insertStmt.setString(1, administrator.getUserName());
			insertStmt.executeUpdate();
			return administrator;
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

	/**
	 * Update the LastName of the Administrators instance.
	 * This runs a UPDATE statement.
	 */
	public Administrators updateUserName(Administrators administrator, String newUserName) throws SQLException {
		// The field to update only exists in the superclass table, so we can
		// just call the superclass method.
		super.updateUserName(administrator, newUserName);
		return administrator;
	}

	/**
	 * Delete the Administrators instance.
	 * This runs a DELETE statement.
	 */
	public Administrators delete(Administrators administrator) throws SQLException {
		String deleteAdministrator = "DELETE FROM Administrators WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteAdministrator);
			deleteStmt.setString(1, administrator.getUserName());
			deleteStmt.executeUpdate();

			// Then also delete from the superclass.
			// Note: due to the fk constraint (ON DELETE CASCADE), we could simply call
			// super.delete() without even needing to delete from Administrators first.
			super.delete(administrator);

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
	
	public Administrators getAdministratorFromUserName(String userName) throws SQLException {
		// To build an Administrator object, we need the Persons record, too.
		String selectAdministrator =
			"SELECT Administrators.UserName AS UserName" +
			"FROM Administrators INNER JOIN Persons " +
			"  ON Administrators.UserName = Persons.UserName " +
			"WHERE Administrators.UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectAdministrator);
			selectStmt.setString(1, userName);
			results = selectStmt.executeQuery();
			if(results.next()) {
				String resultUserName = results.getString("UserName");
				Administrators administrator = new Administrators(resultUserName);
				return administrator;
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

}
