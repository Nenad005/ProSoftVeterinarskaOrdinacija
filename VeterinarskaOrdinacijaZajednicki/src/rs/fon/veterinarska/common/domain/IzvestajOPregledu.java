package rs.fon.veterinarska.common.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author nenad
 */
public class IzvestajOPregledu implements IAbstractDomainObject {
    private Integer idIzvestaj;
    private LocalDateTime datumPregleda;
    private String razlogDolaska;
    private String anamneza;
    private String dijagnoza;
    private String terapija;
    private String imePrezimeVlasnika;
    private String telefonVlasnika;
    private String napomena;
    private BigDecimal ukupnaCena;
    private Veterinar veterinar;
    private Zivotinja zivotinja;
    private ArrayList<StavkaIzvestaja> stavke;
    
    public IzvestajOPregledu(){}

    public IzvestajOPregledu(Integer idIzvestaj, LocalDateTime datumPregleda,
            String razlogDolaska, String anamneza, String dijagnoza, String terapija,
            String imePrezimeVlasnika, String telefonVlasnika, String napomena,
            BigDecimal ukupnaCena, Veterinar veterinar, Zivotinja zivotinja) {
        this.idIzvestaj = idIzvestaj;
        this.datumPregleda = datumPregleda;
        this.razlogDolaska = razlogDolaska;
        this.anamneza = anamneza;
        this.dijagnoza = dijagnoza;
        this.terapija = terapija;
        this.imePrezimeVlasnika = imePrezimeVlasnika;
        this.telefonVlasnika = telefonVlasnika;
        this.napomena = napomena;
        this.ukupnaCena = ukupnaCena;
        this.veterinar = veterinar;
        this.zivotinja = zivotinja;
    }

    public Integer getIdIzvestaj() {
        return idIzvestaj;
    }

    public Veterinar getVeterinar() {
        return veterinar;
    }

    public Zivotinja getZivotinja() {
        return zivotinja;
    }

    public ArrayList<StavkaIzvestaja> getStavke() {
        return stavke;
    }

    @Override
    public int getRelatedObjectsCount() {
        return 3;
    }

    @Override
    public IAbstractDomainObject getRelatedObject(int index) {
        switch (index) {
            case 0:
                return veterinar;
            case 1:
                return zivotinja;
            case 2:
                return new StavkaIzvestaja(this, null, null, null, null);
            default:
                throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
        }
    }

    @Override
    public boolean isRelatedObjectCollection(int index) {
        return index == 2;
    }

    @Override
    public void setRelatedObjects(int index, ArrayList<IAbstractDomainObject> objects) {
        switch (index) {
            case 0:
                veterinar = (Veterinar) objects.get(0);
                break;
            case 1:
                zivotinja = (Zivotinja) objects.get(0);
                break;
            case 2:
                stavke = new ArrayList<>();
                for (IAbstractDomainObject object : objects) {
                    StavkaIzvestaja stavka = (StavkaIzvestaja) object;
                    stavka.setIzvestaj(this);
                    stavke.add(stavka);
                }
                break;
            default:
                throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
        }
    }

    @Override
    public String getTableName() {
        return "IzvestajOPregledu";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            IzvestajOPregledu object = new IzvestajOPregledu();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        idIzvestaj = rs.getInt("idIzvestaj");
        datumPregleda = rs.getTimestamp("datumPregleda").toLocalDateTime();
        razlogDolaska = rs.getString("razlogDolaska");
        anamneza = rs.getString("anamneza");
        dijagnoza = rs.getString("dijagnoza");
        terapija = rs.getString("terapija");
        imePrezimeVlasnika = rs.getString("imePrezimeVlasnika");
        telefonVlasnika = rs.getString("telefonVlasnika");
        napomena = rs.getString("napomena");
        ukupnaCena = rs.getBigDecimal("ukupnaCena");
        veterinar = new Veterinar(rs.getInt("idVeterinar"),
                null, null, null, null, null, null);
        zivotinja = new Zivotinja(rs.getInt("idZivotinja"),
                null, null, Pol.NEPOZNAT, null, null, null);
        // Stavke nisu kolone ovog reda i ucitavaju se zasebno.
        stavke = null;
    }

    @Override
    public String getColumnsForInsert() {
        // Ukupnu cenu odrzavaju triggeri na osnovu stavki.
        return "datumPregleda, razlogDolaska, anamneza, dijagnoza, terapija, "
                + "imePrezimeVlasnika, telefonVlasnika, napomena, idVeterinar, idZivotinja";
    }

    @Override
    public String getValuesForInsert() {
        return           sqlString(datumPregleda == null ? null : datumPregleda.toString().replace('T', ' '))
                + ", " + sqlString(razlogDolaska) + ", " + sqlString(anamneza)
                + ", " + sqlString(dijagnoza) + ", " + sqlString(terapija)
                + ", " + sqlString(imePrezimeVlasnika) + ", " + sqlString(telefonVlasnika)
                + ", " + sqlString(napomena)
                + ", " + (veterinar == null ? null : veterinar.getIdVeterinar())
                + ", " + (zivotinja == null ? null : zivotinja.getIdZivotinja());
    }

    @Override
    public String getValuesForUpdate() {
        return      "datumPregleda = " + sqlString(datumPregleda == null ? null : datumPregleda.toString().replace('T', ' '))
                + ", razlogDolaska = " + sqlString(razlogDolaska)
                + ", anamneza = " + sqlString(anamneza)
                + ", dijagnoza = " + sqlString(dijagnoza) + ", terapija = " + sqlString(terapija)
                + ", imePrezimeVlasnika = " + sqlString(imePrezimeVlasnika)
                + ", telefonVlasnika = " + sqlString(telefonVlasnika)
                + ", napomena = " + sqlString(napomena)
                + ", idVeterinar = " + (veterinar == null ? null : veterinar.getIdVeterinar())
                + ", idZivotinja = " + (zivotinja == null ? null : zivotinja.getIdZivotinja());
    }

    @Override
    public String getIdentificationCriteria() {
        return idIzvestaj == null ? "" : "idIzvestaj = " + idIzvestaj;
    }

    @Override
    public String getSelectCriteria() {
        if (idIzvestaj != null) {
            return getIdentificationCriteria();
        }
        ArrayList<String> criteria = new ArrayList<>();
        if (veterinar != null && veterinar.getIdVeterinar() != null) {
            criteria.add("idVeterinar = " + veterinar.getIdVeterinar());
        }
        if (zivotinja != null && zivotinja.getIdZivotinja() != null) {
            criteria.add("idZivotinja = " + zivotinja.getIdZivotinja());
        }
        return String.join(" AND ", criteria);
    }

    private String sqlString(String value) {
        return value == null ? "NULL" : "'" + value.replace("\\", "\\\\").replace("'", "''") + "'";
    }
}
