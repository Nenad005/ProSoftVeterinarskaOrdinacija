package rs.fon.veterinarska.server.so.vrstazivotinje;

import rs.fon.veterinarska.server.so.AbstractSO;
import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.common.domain.VrstaZivotinje;
import rs.fon.veterinarska.server.db.DBConnectionFactory;

/**
 *
 * @author nenad
 */
public class SOKreirajVrstuZivotinje extends AbstractSO {

    private VrstaZivotinje result;

    public SOKreirajVrstuZivotinje(DBConnectionFactory dbFactory) {
        super(dbFactory);
    }

    @Override
    protected void validate(IAbstractDomainObject object) throws Exception {
        // Nema ulaznih podataka: ova SO samo priprema novi objekat za unos.
        result = null;
    }

    @Override
    protected void executeOperation(IAbstractDomainObject object) throws Exception {
        result = new VrstaZivotinje();
    }

    @Override
    protected boolean requiresDBConnection() {
        return false;
    }

    public VrstaZivotinje getResult() {
        return result;
    }
}
