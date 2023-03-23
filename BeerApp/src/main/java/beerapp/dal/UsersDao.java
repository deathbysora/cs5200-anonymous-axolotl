package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.Person;
import beerapp.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Data access object (DAO) class to interact with the underlying BlogUser table in your MySQL
 * instance. This is used to store {@link BlogUser} into your MySQL instance and retrieve
 * {@link BlogUser} from MySQL instance.
 */
public class UsersDao extends PersonsDao {

    private final static String TABLE_NAME = "users";
    private static UsersDao instance = null;

    protected UsersDao() {
        super();
    }

    public static UsersDao getInstance() {
        if (instance == null) {
            instance = new UsersDao();
        }
        return instance;
    }

    public User create(User user) {
        // Insert into the superclass table first.
        super.create(user);

        String insertBlogUser = "INSERT INTO " + TABLE_NAME + " (UserName) VALUES(?);";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement insertStmt = connection.prepareStatement(insertBlogUser)
        ) {
            insertStmt.setString(1, user.getUsername());
            insertStmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateUserName(User user, String newUserName) {
        // The field to update only exists in the superclass table, so we can
        // just call the superclass method.
        Person person = super.updateUsername(user, newUserName);
        return new User(person.getUsername());
    }

    public User delete(User user) {
        String deleteUser = "DELETE FROM " + TABLE_NAME + " WHERE UserName=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement deleteStmt = connection.prepareStatement(deleteUser)
        ) {
            deleteStmt.setString(1, user.getUsername());
            int affectedRows = deleteStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException(
                  "No records available to delete for Username=" + user.getUsername());
            }

            super.delete(user);

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserFromUserName(String userName) {
        String selectUser =
          "SELECT " + TABLE_NAME + ".UserName AS UserName " +
            "FROM " + TABLE_NAME + " INNER JOIN Persons " +
            "ON " + TABLE_NAME + ".UserName = Persons.UserName " +
            "WHERE " + TABLE_NAME + ".UserName=?;";
        ResultSet results = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement selectStmt = connection.prepareStatement(selectUser)
        ) {
            selectStmt.setString(1, userName);
            results = selectStmt.executeQuery();
            if (results.next()) {
                String resultUserName = results.getString("UserName");
                return new User(resultUserName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(results);
        }
        return null;
    }

}