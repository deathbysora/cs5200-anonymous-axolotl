package beerapp.dal;

import static beerapp.dal.Utility.safeCloseResultSet;

import beerapp.model.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonsDao {

    private final static String TABLE_NAME = "Persons";
    private static PersonsDao instance = null;
    protected ConnectionManager connectionManager;

    protected PersonsDao() {
        connectionManager = new ConnectionManager();
    }

    public static PersonsDao getInstance() {
        if (instance == null) {
            instance = new PersonsDao();
        }
        return instance;
    }

    public Person create(Person person) throws SQLException {
        String insertPerson = "INSERT INTO " + TABLE_NAME + " (UserName) VALUES(?);";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement insertStmt = connection.prepareStatement(insertPerson)
        ) {
            insertStmt.setString(1, person.getUsername());
            insertStmt.executeUpdate();
            return person;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person updateUsername(Person person, String newUsername) throws SQLException {
        String updatePerson = "UPDATE " + TABLE_NAME + " SET UserName=? WHERE UserName=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement updateStmt = connection.prepareStatement(updatePerson)
        ) {
            updateStmt.setString(1, newUsername);
            updateStmt.setString(2, person.getUsername());
            updateStmt.executeUpdate();
            return new Person(newUsername);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person delete(Person person) throws SQLException {
        String deletePerson = "DELETE FROM " + TABLE_NAME + " WHERE UserName=?;";
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement deleteStmt = connection.prepareStatement(deletePerson)
        ) {
            deleteStmt.setString(1, person.getUsername());
            deleteStmt.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Person getPersonByUsername(String userName) throws SQLException {
        String selectPerson = "SELECT UserName FROM " + TABLE_NAME + " WHERE UserName=?;";
        ResultSet result = null;
        try (
          Connection connection = connectionManager.getConnection();
          PreparedStatement selectStmt = connection.prepareStatement(selectPerson)
        ) {
            selectStmt.setString(1, userName);
            result = selectStmt.executeQuery();
            if (result.next()) {
                String resultUsername = result.getString("Username");
                return new Person(resultUsername);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeCloseResultSet(result);
        }
        return null;
    }
}