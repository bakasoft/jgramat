package org.bakasoft.gramat;

public class GrammarException extends RuntimeException {

    private final Location location;

    public GrammarException() {
        this(null, null, null);
    }

    public GrammarException(String message, Location location) {
        this(message, location, null);
    }

    public GrammarException(String message, Location location, Throwable cause) {
        super(message + (location != null ? " (" + location.toString() + ")" : ""), cause);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
