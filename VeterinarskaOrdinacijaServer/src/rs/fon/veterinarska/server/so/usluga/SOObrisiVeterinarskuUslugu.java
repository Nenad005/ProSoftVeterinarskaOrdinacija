package rs.fon.veterinarska.server.so.usluga;

import rs.fon.veterinarska.server.so.AbstractSO;
import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.server.db.DBConnectionFactory;

/**
 *
 * @author nenad
 */
public class SOObrisiVeterinarskuUslugu extends AbstractSO {

    public SOObrisiVeterinarskuUslugu(DBConnectionFactory dbFactory) {
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
