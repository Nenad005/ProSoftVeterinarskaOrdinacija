package rs.fon.veterinarska.common.domain;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Veterinar i njegove strucne kvalifikacije.
 *
 * @author nenad
 */
public class Veterinar implements IAbstractDomainObject {
    private Integer idVeterinar;
    private String ime;
    private String prezime;
    private String brojLicence;
    private String telefon;
    private String email;
    // Hash ne sme biti deo objekta koji server salje klijentu.
    private transient String lozinkaHash;
    private ArrayList<KvalifikacijaVeterinara> kvalifikacije;

    public Veterinar() {
    }

    public Veterinar(Integer idVeterinar, String ime, String prezime,
            String brojLicence, String telefon, String email, String lozinkaHash) {
        this.idVeterinar = idVeterinar;
        this.ime = ime;
        this.prezime = prezime;
        this.brojLicence = brojLicence;
        this.telefon = telefon;
        this.email = email;
        this.lozinkaHash = lozinkaHash;
    }

    @Override
    public String toString() {
        return ((ime == null ? "" : ime) + " "
                + (prezime == null ? "" : prezime)).trim();
    }

    public Integer getIdVeterinar() {
        return idVeterinar;
    }

    public ArrayList<KvalifikacijaVeterinara> getKvalifikacije() {
        return kvalifikacije;
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
        return new KvalifikacijaVeterinara(this, null, null);
    }

    @Override
    public boolean isRelatedObjectCollection(int index) {
        return index == 0;
    }

    @Override
    public void setRelatedObjects(int index, ArrayList<IAbstractDomainObject> objects) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
        }
        kvalifikacije = new ArrayList<>();
        for (IAbstractDomainObject object : objects) {
            KvalifikacijaVeterinara kvalifikacija = (KvalifikacijaVeterinara) object;
            kvalifikacija.setVeterinar(this);
            kvalifikacije.add(kvalifikacija);
        }
    }

    @Override
    public String getTableName() {
        return "Veterinar";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            Veterinar object = new Veterinar();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        idVeterinar = rs.getInt("idVeterinar");
        ime = rs.getString("ime");
        prezime = rs.getString("prezime");
        brojLicence = rs.getString("brojLicence");
        telefon = rs.getString("telefon");
        email = rs.getString("email");
        lozinkaHash = rs.getString("lozinkaHash");
        // Kolekcija se ucitava zasebno kroz rowSelectRelated().
        kvalifikacije = null;
    }

    @Override
    public String getColumnsForInsert() {
        return "ime, prezime, brojLicence, telefon, email, lozinkaHash";
    }

    @Override
    public String getValuesForInsert() {
        return sqlString(ime) + ", " + sqlString(prezime) + ", " + sqlString(brojLicence)
                + ", " + sqlString(telefon) + ", " + sqlString(email) + ", " + sqlString(lozinkaHash);
    }

    @Override
    public String getValuesForUpdate() {
        String values = "ime = " + sqlString(ime) + ", prezime = " + sqlString(prezime)
                + ", brojLicence = " + sqlString(brojLicence)
                + ", telefon = " + sqlString(telefon) + ", email = " + sqlString(email);
        // Transient hash nedostaje u objektu primljenom od klijenta;
        // izmena ostalih podataka ne sme da ga obrise.
        if (lozinkaHash != null) {
            values += ", lozinkaHash = " + sqlString(lozinkaHash);
        }
        return values;
    }

    @Override
    public String getIdentificationCriteria() {
        return idVeterinar == null ? "" : "idVeterinar = " + idVeterinar;
    }

    @Override
    public String getSelectCriteria() {
        if (idVeterinar != null) {
            return getIdentificationCriteria();
        }
        ArrayList<String> criteria = new ArrayList<>();
        if (ime != null && !ime.isBlank()) {
            criteria.add("ime LIKE " + sqlString("%" + ime + "%"));
        }
        if (prezime != null && !prezime.isBlank()) {
            criteria.add("prezime LIKE " + sqlString("%" + prezime + "%"));
        }
        if (email != null && !email.isBlank()) {
            criteria.add("email = " + sqlString(email));
        }
        return String.join(" AND ", criteria);
    }

    private String sqlString(String value) {
        return value == null ? "NULL"
                : "'" + value.replace("\\", "\\\\").replace("'", "''") + "'";
    }
}
