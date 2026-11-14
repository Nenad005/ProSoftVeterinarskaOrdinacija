package rs.fon.veterinarska.common.domain;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Identitet u bazi je par (izvestaj.idIzvestaj, rb).
 * Cena je sacuvana cena stavke, a ne dinamicki pogled na cenovnik.
 * Setteri sluze i za ucitavanje iz baze; zabranu promene sacuvane usluge/cene
 * proveravaju server i baza, ne stanje objekta koje salje klijent.
 *
 * @author nenad
 */
public class StavkaIzvestaja implements IAbstractDomainObject {
    private IzvestajOPregledu izvestaj;
    private Integer rb;
    private String napomena;
    private BigDecimal cena;
    private VeterinarskaUsluga usluga;

    public StavkaIzvestaja() {}

    public StavkaIzvestaja(IzvestajOPregledu izvestaj, Integer rb,
            String napomena, BigDecimal cena, VeterinarskaUsluga usluga) {
        this.izvestaj = izvestaj;
        this.rb = rb;
        this.napomena = napomena;
        this.cena = cena;
        this.usluga = usluga;
    }

    @Override
    public String toString() {
        return (rb == null ? "" : rb + ". ")
                + (usluga == null ? "" : usluga.toString());
    }

    public VeterinarskaUsluga getUsluga() {
        return usluga;
    }

    public IzvestajOPregledu getIzvestaj() {
        return izvestaj;
    }

    public void setIzvestaj(IzvestajOPregledu izvestaj) {
        this.izvestaj = izvestaj;
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
        // Roditelj se ne ucitava ponovo: izvestaj -> stavke -> izvestaj bi bio ciklus.
        return usluga;
    }

    @Override
    public void setRelatedObjects(int index, ArrayList<IAbstractDomainObject> objects) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
        }
        usluga = (VeterinarskaUsluga) objects.get(0);
    }

    @Override
    public String getTableName() {
        return "StavkaIzvestaja";
    }

    @Override
    public ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        ArrayList<IAbstractDomainObject> result = new ArrayList<>();
        while (rs.next()) {
            StavkaIzvestaja object = new StavkaIzvestaja();
            object.populateFromRow(rs);
            result.add(object);
        }
        return result;
    }

    @Override
    public void populateFromRow(ResultSet rs) throws SQLException {
        izvestaj = new IzvestajOPregledu(rs.getInt("idIzvestaj"),
                null, null, null, null, null, null, null, null, null, null, null);
        rb = rs.getInt("rb");
        napomena = rs.getString("napomena");
        cena = rs.getBigDecimal("cena");
        usluga = new VeterinarskaUsluga(rs.getInt("idUsluga"), null, null, null);
    }

    @Override
    public String getColumnsForInsert() {
        // Cena pri unosu dolazi iz cenovnika preko triggera stavka_bi.
        return "idIzvestaj, rb, napomena, idUsluga";
    }

    @Override
    public String getValuesForInsert() {
        return (izvestaj == null ? null : izvestaj.getIdIzvestaj()) + ", "
                + rb + ", " + sqlString(napomena) + ", "
                + (usluga == null ? null : usluga.getIdUsluga());
    }

    @Override
    public String getValuesForUpdate() {
        // Trigger odbija promenu sacuvane cene ili usluge.
        return "napomena = " + sqlString(napomena) + ", cena = " + cena
                + ", idUsluga = " + (usluga == null ? null : usluga.getIdUsluga());
    }

    @Override
    public String getIdentificationCriteria() {
        if (izvestaj == null || izvestaj.getIdIzvestaj() == null || rb == null) {
            return "";
        }
        return "idIzvestaj = " + izvestaj.getIdIzvestaj() + " AND rb = " + rb;
    }

    @Override
    public String getSelectCriteria() {
        ArrayList<String> criteria = new ArrayList<>();
        if (izvestaj != null && izvestaj.getIdIzvestaj() != null) {
            criteria.add("idIzvestaj = " + izvestaj.getIdIzvestaj());
        }
        if (rb != null) {
            criteria.add("rb = " + rb);
        }
        return String.join(" AND ", criteria);
    }

    @Override
    public String orderBy() {
        return "ORDER BY idIzvestaj, rb";
    }

    private String sqlString(String value) {
        return value == null ? "NULL"
                : "'" + value.replace("\\", "\\\\").replace("'", "''") + "'";
    }
}
