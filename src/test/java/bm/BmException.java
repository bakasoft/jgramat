package bm;

public class BmException extends RuntimeException {
    public BmException(String message, Exception cause) {
        super(message, cause);
    }
}
