package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.Administrator;
import beerapp.model.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministratorsDao extends PersonsDao {

    private final static String TABLE_NAME = "Administrators";
    private static AdministratorsDao instance = null;

    protected AdministratorsDao() {
        super();
    }

    public static AdministratorsDao getInstance() {
        if (instance == null) {
            instance = new AdministratorsDao();
        }
        return instance;
    }

    public Administrator create(Administrator administrator) throws SQLException {
        create(new Person(administrator.getUsername()));
        String insertAdministrator = "INSERT INTO " + TABLE_NAME + " (UserName) VALUES(?);";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement insertStmt = connection.prepareStatement(insertAdministrator)
        ) {
            insertStmt.setString(1, administrator.getUsername());
            insertStmt.executeUpdate();
            return administrator;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }

    public Administrator updateUserName(Administrator administrator, String newUserName)
      throws SQLException {
        // The field to update only exists in the superclass table, so we can
        // just call the superclass method.
        Person person = super.updateUsername(administrator, newUserName);
        return new Administrator(person.getUsername());
    }

    public Administrator delete(Administrator administrator) throws SQLException {
        String deleteAdministrator = "DELETE FROM " + TABLE_NAME + " WHERE UserName=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement deleteStmt = connection.prepareStatement(deleteAdministrator)
        ) {
            deleteStmt.setString(1, administrator.getUsername());
            deleteStmt.executeUpdate();

            // Then also delete from the superclass.
            // Note: due to the fk constraint (ON DELETE CASCADE), we could simply call
            // super.delete() without even needing to delete from Administrator first.
            super.delete(administrator);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Administrator getAdministratorFromUserName(String userName) {
        // To build an Administrator object, we need the Persons record, too.
        String selectAdministrator =
          "SELECT " + TABLE_NAME + ".UserName AS UserName " +
            "FROM " + TABLE_NAME + " INNER JOIN Persons " +
            "  ON " + TABLE_NAME + ".UserName = Persons.UserName " +
            "WHERE " + TABLE_NAME + ".UserName=?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement selectStmt = connection.prepareStatement(selectAdministrator)

        ) {
            selectStmt.setString(1, userName);
            results = selectStmt.executeQuery();
            if (results.next()) {
                String resultUserName = results.getString("UserName");
                Administrator administrator = new Administrator(resultUserName);
                return administrator;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
        return null;
    }

}