package rs.fon.veterinarska.server.db;
import java.sql.*;
/**
 *
 * @author nenad
 */
public class DBConnectionFactory {
    private final String url;
    private final String username;
    private final String password;

    public DBConnectionFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public Connection openConnection() throws SQLException {
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            try {
                connection.setAutoCommit(false);
            } catch (SQLException ex) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    ex.addSuppressed(closeEx);
                }
                throw ex;
            }
            
            return connection;
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
