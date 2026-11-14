package rs.fon.veterinarska.common.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nenad
 */
public class Zivotinja implements IAbstractDomainObject {
    private Integer idZivotinja;
    private String ime;
    private Integer godRodjenja;
    private Pol pol = Pol.NEPOZNAT;
    private String brojMikrocipa;
    private String rasa;
    private VrstaZivotinje vrsta;

    public Zivotinja() {
    }

    public Zivotinja(Integer idZivotinja, String ime, Integer godRodjenja,
            Pol pol, String brojMikrocipa, String rasa, VrstaZivotinje vrsta) {
        this.idZivotinja = idZivotinja;
        this.ime = ime;
        this.godRodjenja = godRodjenja;
        this.pol = pol;
        this.brojMikrocipa = brojMikrocipa;
        this.rasa = rasa;
        this.vrsta = vrsta;
    }

    @Override
    public String toString() {
        return ime == null ? "" : ime;
    }

    public Integer getIdZivotinja() {
        return idZivotinja;
    }

    public VrstaZivotinje getVrsta() {
        return vrsta;
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
        return vrsta;
    }

    @Override
    public void setRelatedObjects(int index, ArrayList<IAbstractDomainObject> objects) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
        }
        vrsta = (VrstaZivotinje) objects.get(0);
    }

    @Override
    public String getTableName() {
        return "Zivotinja";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            Zivotinja object = new Zivotinja();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        idZivotinja = rs.getInt("idZivotinja");
        ime = rs.getString("ime");
        int godina = rs.getInt("godRodjenja");
        godRodjenja = rs.wasNull() ? null : godina;
        pol = Pol.izVrednostiUBazi(rs.getString("pol"));
        brojMikrocipa = rs.getString("brojMikrocipa");
        rasa = rs.getString("rasa");
        vrsta = new VrstaZivotinje(rs.getInt("idVrsta"), null, null);
    }

    @Override
    public String getColumnsForInsert() {
        return "ime, godRodjenja, pol, brojMikrocipa, rasa, idVrsta";
    }

    @Override
    public String getValuesForInsert() {
        return sqlString(ime) + ", " + godRodjenja + ", "
                + sqlString(pol == null ? null : pol.getVrednostUBazi()) + ", "
                + sqlString(brojMikrocipa) + ", " + sqlString(rasa) + ", "
                + (vrsta == null ? null : vrsta.getIdVrsta());
    }

    @Override
    public String getValuesForUpdate() {
        return "ime = " + sqlString(ime) + ", godRodjenja = " + godRodjenja
                + ", pol = " + sqlString(pol == null ? null : pol.getVrednostUBazi())
                + ", brojMikrocipa = " + sqlString(brojMikrocipa)
                + ", rasa = " + sqlString(rasa)
                + ", idVrsta = " + (vrsta == null ? null : vrsta.getIdVrsta());
    }

    @Override
    public String getIdentificationCriteria() {
        return idZivotinja == null ? "" : "idZivotinja = " + idZivotinja;
    }

    @Override
    public String getSelectCriteria() {
        if (idZivotinja != null) {
            return getIdentificationCriteria();
        }
        ArrayList<String> criteria = new ArrayList<>();
        if (ime != null && !ime.isBlank()) {
            criteria.add("ime LIKE " + sqlString("%" + ime + "%"));
        }
        if (vrsta != null && vrsta.getIdVrsta() != null) {
            criteria.add("idVrsta = " + vrsta.getIdVrsta());
        }
        return String.join(" AND ", criteria);
    }

    private String sqlString(String value) {
        return value == null ? "NULL"
                : "'" + value.replace("\\", "\\\\").replace("'", "''") + "'";
    }
}
