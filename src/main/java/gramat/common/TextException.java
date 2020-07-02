package gramat.common;

import gramat.GramatException;

public class TextException extends GramatException {

    public final TextLocation location;

    public TextException(String message, TextLocation location) {
        super(message + " " + location);
        this.location = location;
    }

}
