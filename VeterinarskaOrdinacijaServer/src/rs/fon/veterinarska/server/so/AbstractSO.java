package rs.fon.veterinarska.server.so;

import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.server.db.DBBroker;
import rs.fon.veterinarska.server.db.DBConnectionFactory;
import java.sql.*;
import java.util.Objects;
/**
 *
 * @author nenad
 */
public abstract class AbstractSO {
    private final DBConnectionFactory dbFactory;
    private Connection connection;
    protected DBBroker db;
    
    protected AbstractSO(DBConnectionFactory dbFactory) {
        this.dbFactory = Objects.requireNonNull(dbFactory, "DBConnectionFactory ne sme biti null.");
    }
    
    // Jedna instanca SO koristi se za jedan zahtev, ne deli se izmedju niti.
    public final void execute(IAbstractDomainObject object) throws Exception {
        Exception failure = null;
        try {
            if (requiresDBConnection()) {
                openDBConnection();
            }
            validate(object);
            executeOperation(object);
            
            if (db != null) {
                db.transactionCommit();
            }
        } catch (Exception ex) {
            failure = ex;
            if (db != null) {
                try {
                    db.transactionRollback();
                } catch (SQLException rollbackEx) {
                    ex.addSuppressed(rollbackEx);
                }
            }
            throw ex;
        } finally {
            try {
                closeDBConnection();
            } catch (SQLException closeEx) {
                if (failure != null) {
                    failure.addSuppressed(closeEx);
                } else {
                    throw closeEx;
                }
            }
        }
    }
        
    void openDBConnection() throws SQLException{
        if (db != null) return;
               
        try {
            connection = dbFactory.openConnection();
            db = new DBBroker(connection);
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    void closeDBConnection() throws SQLException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            db = null;
            connection = null;
        }
    }

    protected boolean requiresDBConnection() {
        return true;
    }

    protected abstract void validate(IAbstractDomainObject object) throws Exception;
 
    protected abstract void executeOperation(IAbstractDomainObject object) throws Exception;
}
