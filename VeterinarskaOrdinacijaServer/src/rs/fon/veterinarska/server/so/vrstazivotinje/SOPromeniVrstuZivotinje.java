package rs.fon.veterinarska.server.so.vrstazivotinje;

import rs.fon.veterinarska.server.so.AbstractSO;
import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.common.domain.VrstaZivotinje;
import java.sql.SQLException;
import java.util.Objects;
import rs.fon.veterinarska.server.db.DBConnectionFactory;

/**
 *
 * @author nenad
 */
public class SOPromeniVrstuZivotinje extends AbstractSO {

    private VrstaZivotinje result;

    public SOPromeniVrstuZivotinje(DBConnectionFactory dbFactory) {
        super(dbFactory);
    }

    @Override
    protected void validate(IAbstractDomainObject object) throws Exception {
        result = null;
        VrstaZivotinje vrsta = (VrstaZivotinje) Objects.requireNonNull(
                object, "Nisu prosledjeni podaci o vrsti zivotinje.");
        if (vrsta.getIdVrsta() == null || vrsta.getIdVrsta() <= 0) {
            throw new IllegalArgumentException("Potreban je pozitivan ID vrste zivotinje.");
        }
        if (vrsta.getNaziv() == null || vrsta.getNaziv().isBlank()) {
            throw new IllegalArgumentException("Naziv vrste zivotinje je obavezan.");
        }
    }

    @Override
    protected void executeOperation(IAbstractDomainObject object) throws Exception {
        VrstaZivotinje vrsta = (VrstaZivotinje) object;
        vrsta.setNaziv(vrsta.getNaziv().trim());
        db.rowUpdate(vrsta);
        // Ponovno citanje dozvoljava i izmenu kod koje su vrednosti ostale iste.
        result = (VrstaZivotinje) db.rowSelectObject(vrsta);
        if (result == null) {
            throw new SQLException("Sistem ne moze da nadje vrstu zivotinje za izmenu.");
        }
    }

    public VrstaZivotinje getResult() {
        return result;
    }
}
