package rs.fon.veterinarska.server.so.vrstazivotinje;

import rs.fon.veterinarska.server.so.AbstractSO;
import rs.fon.veterinarska.common.domain.IAbstractDomainObject;
import rs.fon.veterinarska.common.domain.VrstaZivotinje;
import java.util.ArrayList;
import java.util.Objects;
import rs.fon.veterinarska.server.db.DBConnectionFactory;

/**
 *
 * @author nenad
 */
public class SOVratiListuVrstaZivotinja extends AbstractSO {

    private ArrayList<VrstaZivotinje> result;

    public SOVratiListuVrstaZivotinja(DBConnectionFactory dbFactory) {
        super(dbFactory);
    }

    @Override
    protected void validate(IAbstractDomainObject object) throws Exception {
        result = null;
        VrstaZivotinje vrsta = (VrstaZivotinje) Objects.requireNonNull(
                object, "Nisu prosledjeni kriterijumi pretrage.");
        if (vrsta.getIdVrsta() != null && vrsta.getIdVrsta() <= 0) {
            throw new IllegalArgumentException("ID vrste zivotinje mora biti pozitivan.");
        }
    }

    @Override
    protected void executeOperation(IAbstractDomainObject object) throws Exception {
        result = new ArrayList<>();
        for (IAbstractDomainObject vrsta : db.rowSelect(object)) {
            result.add((VrstaZivotinje) vrsta);
        }
    }

    public ArrayList<VrstaZivotinje> getResult() {
        return result;
    }
}
