package beerapp.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Utility {

    /**
     * @param result The {@link ResultSet} to close.
     */
    public static void safeCloseResultSet(ResultSet result) {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}