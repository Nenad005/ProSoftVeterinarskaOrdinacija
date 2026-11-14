package rs.fon.veterinarska.common.domain;

import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Identitet u bazi je par (veterinar.idVeterinar, kvalifikacija.idKvalifikacija).
 *
 * @author nenad
 */
public class KvalifikacijaVeterinara implements IAbstractDomainObject {
    private Veterinar veterinar;
    private StrucnaKvalifikacija kvalifikacija;
    private LocalDate datumSticanja;

    public KvalifikacijaVeterinara() {
    }

    public KvalifikacijaVeterinara(Veterinar veterinar, StrucnaKvalifikacija kvalifikacija, LocalDate datumSticanja) {
        this.veterinar = veterinar;
        this.kvalifikacija = kvalifikacija;
        this.datumSticanja = datumSticanja;
    }

    @Override
    public String toString() {
        return kvalifikacija == null ? "" : kvalifikacija.toString();
    }

    public StrucnaKvalifikacija getKvalifikacija() {
        return kvalifikacija;
    }

    public Veterinar getVeterinar() {
        return veterinar;
    }

    public void setVeterinar(Veterinar veterinar) {
        this.veterinar = veterinar;
    }

    @Override
    public int getRelatedObjectsCount() {
        return 1;
    }

    @Override
    public IAbstractDomainObject getRelatedObject(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
        }
        // Veterinar je roditelj kolekcije i ne ucitava se ponovo.
        return kvalifikacija;
    }

    @Override
    public void setRelatedObjects(int index, ArrayList<IAbstractDomainObject> objects) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
        }
        kvalifikacija = (StrucnaKvalifikacija) objects.get(0);
    }

    @Override
    public String getTableName() {
        return "KvalifikacijaVeterinara";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            KvalifikacijaVeterinara object = new KvalifikacijaVeterinara();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        veterinar = new Veterinar(rs.getInt("idVeterinar"),
                null, null, null, null, null, null);
        kvalifikacija = new StrucnaKvalifikacija(rs.getInt("idKvalifikacija"), null, null);
        datumSticanja = rs.getDate("datumSticanja").toLocalDate();
    }

    @Override
    public String getColumnsForInsert() {
        return "idVeterinar, idKvalifikacija, datumSticanja";
    }

    @Override
    public String getValuesForInsert() {
        return (veterinar == null ? null : veterinar.getIdVeterinar()) + ", "
                + (kvalifikacija == null ? null : kvalifikacija.getIdKvalifikacija()) + ", "
                + (datumSticanja == null ? "NULL" : "'" + datumSticanja + "'");
    }

    @Override
    public String getValuesForUpdate() {
        // Promena veze (slozenog kljuca) radi se brisanjem i novim unosom.
        return "datumSticanja = "
                + (datumSticanja == null ? "NULL" : "'" + datumSticanja + "'");
    }

    @Override
    public String getIdentificationCriteria() {
        if (veterinar == null || veterinar.getIdVeterinar() == null
                || kvalifikacija == null || kvalifikacija.getIdKvalifikacija() == null) {
            return "";
        }
        return "idVeterinar = " + veterinar.getIdVeterinar()
                + " AND idKvalifikacija = " + kvalifikacija.getIdKvalifikacija();
    }

    @Override
    public String getSelectCriteria() {
        ArrayList<String> criteria = new ArrayList<>();
        if (veterinar != null && veterinar.getIdVeterinar() != null) {
            criteria.add("idVeterinar = " + veterinar.getIdVeterinar());
        }
        if (kvalifikacija != null && kvalifikacija.getIdKvalifikacija() != null) {
            criteria.add("idKvalifikacija = " + kvalifikacija.getIdKvalifikacija());
        }
        return String.join(" AND ", criteria);
    }
}
