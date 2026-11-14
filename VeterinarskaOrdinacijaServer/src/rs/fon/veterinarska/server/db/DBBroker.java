/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.fon.veterinarska.server.db;

import java.util.ArrayList;
import java.sql.*;
import rs.fon.veterinarska.common.domain.IAbstractDomainObject;

/**
 *
 * @author nenad
 */
public class DBBroker {
    
    private final Connection connection;
    
    public DBBroker(Connection connection) {
        this.connection = connection;
    }
    
    public void transactionCommit() throws SQLException{
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public void transactionRollback() throws SQLException{
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public ArrayList<IAbstractDomainObject> rowSelect(IAbstractDomainObject object) throws SQLException{
        String upit = "SELECT * FROM " + object.getTableName()
                + " " + object.alijas() + " " + object.join();
        String criteria = object.getSelectCriteria();
        if (criteria != null && !criteria.isBlank()) {
            upit += " WHERE " + criteria;
        }
        upit += " " + object.orderBy();

        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(upit)) {
            return object.getResults(rs);
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    /** Vraca kompletan objekat sa vezama, ili null ako zapis ne postoji. */
    public IAbstractDomainObject rowSelectObject(IAbstractDomainObject object) throws SQLException{
        String criteria = requireIdentificationCriteria(object);
        String upit = "SELECT * FROM " + object.getTableName()
                + " " + object.alijas() + " " + object.join()
                + " WHERE " + criteria;

        try {
            ArrayList<IAbstractDomainObject> results;
            try (Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(upit)) {
                results = object.getResults(rs);
            }

            if (results.isEmpty()) {
                return null;
            }
            if (results.size() != 1) {
                throw new SQLException("Identifikacioni uslov mora da vrati samo jedan objekat.");
            }

            IAbstractDomainObject result = results.get(0);
            rowSelectRelated(result);
            return result;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    /** Popunjava veze vec ucitanog objekta, koristeci istu konekciju. */
    public void rowSelectRelated(IAbstractDomainObject object) throws SQLException{
        try {
            requireIdentificationCriteria(object);
            for (int i = 0; i < object.getRelatedObjectsCount(); i++) {
                IAbstractDomainObject related = object.getRelatedObject(i);
                if (related == null) {
                    throw new SQLException("Nije definisan objekat za vezu: " + i);
                }

                ArrayList<IAbstractDomainObject> results;
                if (object.isRelatedObjectCollection(i)) {
                    String criteria = related.getSelectCriteria();
                    if (criteria == null || criteria.isBlank()) {
                        throw new SQLException("Ucitavanje vezane kolekcije zahteva uslov.");
                    }
                    results = rowSelect(related);
                    for (IAbstractDomainObject item : results) {
                        rowSelectRelated(item);
                    }
                } else {
                    IAbstractDomainObject result = rowSelectObject(related);
                    if (result == null) {
                        throw new SQLException("Vezani objekat ne postoji: " + related.getTableName());
                    }
                    results = new ArrayList<>();
                    results.add(result);
                }
                object.setRelatedObjects(i, results);
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public boolean rowInsert(IAbstractDomainObject object) throws SQLException{
        String upit = "INSERT INTO " + object.getTableName()
                + " (" + object.getColumnsForInsert() + ")"
                + " VALUES (" + object.getValuesForInsert() + ")";

        try (Statement st = connection.createStatement()) {
            return st.executeUpdate(upit) > 0;
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    /** Unos u tabelu sa jednim AUTO_INCREMENT kljucem; vraca generisani ID. */
    public int rowInsertAndGetId(IAbstractDomainObject object) throws SQLException{
        String upit = "INSERT INTO " + object.getTableName()
                + " (" + object.getColumnsForInsert() + ")"
                + " VALUES (" + object.getValuesForInsert() + ")";

        try (Statement st = connection.createStatement()) {
            if (st.executeUpdate(upit, Statement.RETURN_GENERATED_KEYS) != 1) {
                throw new SQLException("Nije upisan jedan novi zapis.");
            }
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("Baza nije vratila generisani ID.");
                }
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public boolean rowDelete(IAbstractDomainObject object) throws SQLException{
        String criteria = requireIdentificationCriteria(object);
        String upit = "DELETE FROM " + object.getTableName()
                + " WHERE " + criteria;

        try (Statement st = connection.createStatement()) {
            return st.executeUpdate(upit) > 0;
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public boolean rowUpdate(IAbstractDomainObject object) throws SQLException{
        String criteria = requireIdentificationCriteria(object);
        String upit = "UPDATE " + object.getTableName()
                + " SET " + object.getValuesForUpdate()
                + " WHERE " + criteria;

        try (Statement st = connection.createStatement()) {
            return st.executeUpdate(upit) > 0;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private String requireIdentificationCriteria(IAbstractDomainObject object)
            throws SQLException {
        String criteria = object.getIdentificationCriteria();
        if (criteria == null || criteria.isBlank()) {
            throw new SQLException("Operacija zahteva uslov za identifikaciju.");
        }
        return criteria;
    }
}
