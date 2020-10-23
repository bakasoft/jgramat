package gramat.eval;

import gramat.input.Location;

public class RejectedException extends RuntimeException {

    public RejectedException() {
        super();
    }

    public RejectedException(String message) {
        super(message);
    }

    public RejectedException(String message, Location location) {
        super(message + " @ " + location);
    }
}
