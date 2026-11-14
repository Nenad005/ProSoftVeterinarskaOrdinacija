package rs.fon.veterinarska.common.domain;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Zajednicki tip domenskih objekata koji se razmenjuju izmedju klijenta i servera.
 * Konkretne klase treba da predefinisu metode mapiranja kada bude definisana
 * sema baze. Podrazumevane implementacije ne dozvoljavaju neprimetno cuvanje
 * objekta bez implementiranog mapiranja.
 *
 * @author nenad
 */
public interface IAbstractDomainObject extends Serializable {

    default String getTableName() {
        throw new UnsupportedOperationException("Naziv tabele nije definisan.");
    }

    default String alijas() {
        return "";
    }

    /** SQL JOIN klauzule, ili prazan string ako nema povezivanja tabela. */
    default String join() {
        return "";
    }

    /** Popunjava ovaj objekat iz trenutnog reda; ne pomera ResultSet kursor. */
    void populateFromRow(ResultSet rs) throws SQLException;

    /** Prolazi kroz rezultat upita i pravi novi objekat za svaki red. */
    default ArrayList<IAbstractDomainObject> getResults(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Mapiranje rezultata nije implementirano.");
    }

    /** Broj veza koje treba ucitati uz detalje objekta, ne broj redova u bazi. */
    default int getRelatedObjectsCount() {
        return 0;
    }

    /** Objekat sa ID-em za referencu ili sa kriterijumom za kolekciju. */
    default IAbstractDomainObject getRelatedObject(int index) {
        throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
    }

    /** Kolekcija sme da bude prazna; pojedinacna veza mora imati jedan red. */
    default boolean isRelatedObjectCollection(int index) {
        return false;
    }

    /** Domen rasporedjuje ucitane objekte u odgovarajuce polje. */
    default void setRelatedObjects(int index, ArrayList<IAbstractDomainObject> objects) {
        throw new IndexOutOfBoundsException("Nepostojeca veza: " + index);
    }

    /** Imena kolona odvojena zarezima, bez okolnih zagrada. */
    default String getColumnsForInsert() {
        throw new UnsupportedOperationException("Kolone za unos nisu definisane.");
    }

    /**
     * SQL literali odvojeni zarezima, bez okolnih zagrada.
     * Trenutni ugovor koristi SQL fragmente, ne parametrizovane vrednosti;
     * korisnicki unos ne sme se direktno spajati u njih.
     */
    default String getValuesForInsert() {
        throw new UnsupportedOperationException("Vrednosti za unos nisu definisane.");
    }

    /** Dodele kolona = vrednost, odvojene zarezima, bez reci SET. */
    default String getValuesForUpdate() {
        throw new UnsupportedOperationException("Vrednosti za izmenu nisu definisane.");
    }

    /** Uslov identifikacije za izmenu/brisanje, bez reci WHERE. */
    default String getIdentificationCriteria() {
        throw new UnsupportedOperationException("Uslov za identifikaciju nije definisan.");
    }

    /** Uslov za SELECT liste bez reci WHERE; prazan string za sve redove. */
    default String getSelectCriteria() {
        return "";
    }

    /** Cela ORDER BY klauzula, ili prazan string ako nema sortiranja. */
    default String orderBy() {
        return "";
    }
}
