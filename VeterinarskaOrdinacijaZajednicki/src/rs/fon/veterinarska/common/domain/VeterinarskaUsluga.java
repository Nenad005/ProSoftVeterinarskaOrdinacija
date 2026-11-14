package rs.fon.veterinarska.common.domain;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Usluga iz cenovnika. Njena cena ne menja cene postojecih stavki.
 *
 * @author nenad
 */
public class VeterinarskaUsluga implements IAbstractDomainObject {
    private Integer idUsluga;
    private String naziv;
    private String opis;
    private BigDecimal cena;

    public VeterinarskaUsluga() {
    }

    public VeterinarskaUsluga(Integer idUsluga, String naziv, String opis, BigDecimal cena) {
        this.idUsluga = idUsluga;
        this.naziv = naziv;
        this.opis = opis;
        this.cena = cena;
    }

    @Override
    public String toString() {
        return naziv == null ? "" : naziv;
    }

    public Integer getIdUsluga() {
        return idUsluga;
    }

    @Override
    public String getTableName() {
        return "VeterinarskaUsluga";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            VeterinarskaUsluga object = new VeterinarskaUsluga();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        idUsluga = rs.getInt("idUsluga");
        naziv = rs.getString("naziv");
        opis = rs.getString("opis");
        cena = rs.getBigDecimal("cena");
    }

    @Override
    public String getColumnsForInsert() {
        return "naziv, opis, cena";
    }

    @Override
    public String getValuesForInsert() {
        return sqlString(naziv) + ", " + sqlString(opis) + ", " + cena;
    }

    @Override
    public String getValuesForUpdate() {
        return "naziv = " + sqlString(naziv) + ", opis = " + sqlString(opis)
                + ", cena = " + cena;
    }

    @Override
    public String getIdentificationCriteria() {
        return idUsluga == null ? "" : "idUsluga = " + idUsluga;
    }

    @Override
    public String getSelectCriteria() {
        if (idUsluga != null) {
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
