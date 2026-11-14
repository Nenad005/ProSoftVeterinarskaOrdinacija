package rs.fon.veterinarska.common.communication;

import java.io.Serializable;

/**
 *
 * @author nenad
 */
public class ServerResponse implements Serializable {
    
    private Object response;
    private String message;
    private ResponseStatus status;

    public ServerResponse() {
    }

    public ServerResponse(
            Object response,
            String message,
            ResponseStatus status) {

        this.response = response;
        this.message = message;
        this.status = status;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}