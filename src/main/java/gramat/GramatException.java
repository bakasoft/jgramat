package gramat;

public class GramatException extends RuntimeException {

    public GramatException(String message) {
        super(message);
    }

    public GramatException(Exception e) {
        super(e);
    }
}
