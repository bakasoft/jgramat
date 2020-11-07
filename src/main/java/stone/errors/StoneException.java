package stone.errors;

public class StoneException extends RuntimeException {

    public StoneException() {
        // TODO remove empty constructor
    }

    public StoneException(String message) {
        super(message);
    }

    public StoneException(Throwable cause) {
        super(cause);
        // TODO add message
    }
}
