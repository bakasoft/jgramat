package gramat;

public class GramatException extends RuntimeException {

    public GramatException(String message) {
        super(message);
    }

    public GramatException(Exception cause) {
        super(cause);
    }

    public GramatException(Exception cause, String message) {
        super(message, cause);
    }

}
