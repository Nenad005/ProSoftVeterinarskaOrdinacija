package rs.fon.veterinarska.server.so.izvestaj;

import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.server.db.DBConnectionFactory;
import rs.fon.veterinarska.server.so.AbstractSO;

/**
 *
 * @author nenad
 */
public class SOKreirajIzvestajOPregledu extends AbstractSO {
    
    public SOKreirajIzvestajOPregledu(DBConnectionFactory dbFactory) {
        super(dbFactory);
    }

    @Override
    protected void validate(IAbstractDomainObject object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void executeOperation(IAbstractDomainObject object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
