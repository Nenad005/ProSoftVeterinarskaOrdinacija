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
public class SOObrisiVrstuZivotinje extends AbstractSO {

    private boolean result;

    public SOObrisiVrstuZivotinje(DBConnectionFactory dbFactory) {
        super(dbFactory);
    }

    @Override
    protected void validate(IAbstractDomainObject object) throws Exception {
        result = false;
        VrstaZivotinje vrsta = (VrstaZivotinje) Objects.requireNonNull(
                object, "Nije prosledjena vrsta zivotinje.");
        if (vrsta.getIdVrsta() == null || vrsta.getIdVrsta() <= 0) {
            throw new IllegalArgumentException("Potreban je pozitivan ID vrste zivotinje.");
        }
    }

    @Override
    protected void executeOperation(IAbstractDomainObject object) throws Exception {
        if (!db.rowDelete(object)) {
            throw new SQLException("Sistem ne moze da nadje vrstu zivotinje za brisanje.");
        }
        result = true;
    }

    public boolean getResult() {
        return result;
    }
}
