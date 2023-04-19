package beerapp.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private final String user = "root";
    private final String password = "Password123!";
    private final String hostName = "127.0.0.1";
    private final int port = 3306;
    private final String schema = "beerapp";
    private final String timezone = "UTC";

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", this.user);
            connectionProperties.put("password", this.password);
            connectionProperties.put("serverTimezone", this.timezone);
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException(e);
            }
            connection = DriverManager.getConnection(
              "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema
                + "?useSSL=false&allowPublicKeyRetrieval=true",
              connectionProperties);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return connection;
    }

    public void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}