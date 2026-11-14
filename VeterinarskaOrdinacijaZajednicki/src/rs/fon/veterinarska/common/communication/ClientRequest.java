package rs.fon.veterinarska.common.communication;

import java.io.Serializable;

/**
 *
 * @author nenad
 */
public class ClientRequest implements Serializable {

    private int operation;
    private Object parameter;

    public ClientRequest() {
    }

    public ClientRequest(int operation, Object parameter) {
        this.operation = operation;
        this.parameter = parameter;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }
}