package rs.fon.veterinarska.common.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nenad
 */
public class VrstaZivotinje implements IAbstractDomainObject {
    private Integer idVrsta;
    private String naziv;
    private String napomene;

    public VrstaZivotinje() {
    }

    public VrstaZivotinje(Integer idVrsta, String naziv, String napomene) {
        this.idVrsta = idVrsta;
        this.naziv = naziv;
        this.napomene = napomene;
    }

    @Override
    public String toString() {
        return naziv == null ? "" : naziv;
    }

    public Integer getIdVrsta() {
        return idVrsta;
    }

    public void setIdVrsta(Integer idVrsta) {
        this.idVrsta = idVrsta;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getNapomene() {
        return napomene;
    }

    public void setNapomene(String napomene) {
        this.napomene = napomene;
    }

    @Override
    public String getTableName() {
        return "VrstaZivotinje";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            VrstaZivotinje object = new VrstaZivotinje();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        idVrsta = rs.getInt("idVrsta");
        naziv = rs.getString("naziv");
        napomene = rs.getString("napomene");
    }

    @Override
    public String getColumnsForInsert() {
        return "naziv, napomene";
    }

    @Override
    public String getValuesForInsert() {
        return sqlString(naziv) + ", " + sqlString(napomene);
    }

    @Override
    public String getValuesForUpdate() {
        return "naziv = " + sqlString(naziv) + ", napomene = " + sqlString(napomene);
    }

    @Override
    public String getIdentificationCriteria() {
        return idVrsta == null ? "" : "idVrsta = " + idVrsta;
    }

    @Override
    public String getSelectCriteria() {
        if (idVrsta != null) {
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
