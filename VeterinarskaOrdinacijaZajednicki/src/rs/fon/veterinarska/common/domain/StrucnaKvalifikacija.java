package rs.fon.veterinarska.common.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nenad
 */
public class StrucnaKvalifikacija implements IAbstractDomainObject {
    private Integer idKvalifikacija;
    private String naziv;
    private String opis;

    public StrucnaKvalifikacija() {
    }

    public StrucnaKvalifikacija(Integer idKvalifikacija, String naziv, String opis) {
        this.idKvalifikacija = idKvalifikacija;
        this.naziv = naziv;
        this.opis = opis;
    }
    
    @Override
    public String toString() {
        return naziv == null ? "" : naziv;
    }

    public Integer getIdKvalifikacija() {
        return idKvalifikacija;
    }

    @Override
    public String getTableName() {
        return "StrucnaKvalifikacija";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            StrucnaKvalifikacija object = new StrucnaKvalifikacija();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        idKvalifikacija = rs.getInt("idKvalifikacija");
        naziv = rs.getString("naziv");
        opis = rs.getString("opis");
    }

    @Override
    public String getColumnsForInsert() {
        return "naziv, opis";
    }

    @Override
    public String getValuesForInsert() {
        return sqlString(naziv) + ", " + sqlString(opis);
    }

    @Override
    public String getValuesForUpdate() {
        return "naziv = " + sqlString(naziv) + ", opis = " + sqlString(opis);
    }

    @Override
    public String getIdentificationCriteria() {
        return idKvalifikacija == null ? "" : "idKvalifikacija = " + idKvalifikacija;
    }

    @Override
    public String getSelectCriteria() {
        if (idKvalifikacija != null) {
            return getIdentificationCriteria();
        }
        return naziv == null || naziv.isBlank() ? ""
                : "naziv LIKE " + sqlString("%" + naziv + "%");
    }

    private String sqlString(String value) {
        return value == null ? "NULL"
                : "'" + value.replace("\\", "\\\\").replace("'", "''") + "'";
    }
}
