package rs.fon.veterinarska.server.so.vrstazivotinje;

import java.sql.SQLException;
import java.util.Objects;
import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.common.domain.VrstaZivotinje;
import rs.fon.veterinarska.server.db.DBConnectionFactory;
import rs.fon.veterinarska.server.so.AbstractSO;

/**
 * Upisuje novu vrstu nakon sto korisnik popuni podatke.
 *
 * @author nenad
 */
public class SOZapamtiVrstuZivotinje extends AbstractSO {

    private VrstaZivotinje result;

    public SOZapamtiVrstuZivotinje(DBConnectionFactory dbFactory) {
        super(dbFactory);
    }

    @Override
    protected void validate(IAbstractDomainObject object) throws Exception {
        result = null;
        VrstaZivotinje vrsta = (VrstaZivotinje) Objects.requireNonNull(
                object, "Nisu prosledjeni podaci o vrsti zivotinje.");
        if (vrsta.getIdVrsta() != null) {
            throw new IllegalArgumentException("Za novu vrstu ID dodeljuje baza.");
        }
        if (vrsta.getNaziv() == null || vrsta.getNaziv().isBlank()) {
            throw new IllegalArgumentException("Naziv vrste zivotinje je obavezan.");
        }
    }

    @Override
    protected void executeOperation(IAbstractDomainObject object) throws Exception {
        VrstaZivotinje vrsta = (VrstaZivotinje) object;
        vrsta.setNaziv(vrsta.getNaziv().trim());
        int id = db.rowInsertAndGetId(vrsta);
        result = (VrstaZivotinje) db.rowSelectObject(new VrstaZivotinje(id, null, null));
        if (result == null) {
            throw new SQLException("Sistem ne moze da ucita sacuvanu vrstu zivotinje.");
        }
    }

    public VrstaZivotinje getResult() {
        return result;
    }
}
