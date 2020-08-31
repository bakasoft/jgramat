package gramat;

import gramat.input.Location;

public class InvalidSyntaxException extends RuntimeException {

    public InvalidSyntaxException(String message, Location location) {
        super(message + " " + location);
    }

}
