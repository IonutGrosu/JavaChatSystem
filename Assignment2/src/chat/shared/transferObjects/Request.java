package chat.shared.transferObjects;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private Object arg;

    public Request(RequestType type, Object arg) {
        this.type = type;
        this.arg = arg;
    }

    public RequestType getType() {
        return type;
    }

    public Object getArg() {
        return arg;
    }

    public String toString() {
        return type + ", " + arg.toString();
    }
}
