package rs.fon.veterinarska.server.so.vrstazivotinje;

import rs.fon.veterinarska.server.so.AbstractSO;
import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.common.domain.VrstaZivotinje;
import java.util.ArrayList;
import rs.fon.veterinarska.server.db.DBConnectionFactory;

/**
 *
 * @author nenad
 */
public class SOVratiListuSvihVrstaZivotinja extends AbstractSO {

    private ArrayList<VrstaZivotinje> result;

    public SOVratiListuSvihVrstaZivotinja(DBConnectionFactory dbFactory) {
        super(dbFactory);
    }

    @Override
    protected void validate(IAbstractDomainObject object) throws Exception {
        // Ulazni kriterijum nije potreban za ucitavanje svih vrsta.
        result = null;
    }

    @Override
    protected void executeOperation(IAbstractDomainObject object) throws Exception {
        result = new ArrayList<>();
        for (IAbstractDomainObject vrsta : db.rowSelect(new VrstaZivotinje())) {
            result.add((VrstaZivotinje) vrsta);
        }
    }

    public ArrayList<VrstaZivotinje> getResult() {
        return result;
    }
}
