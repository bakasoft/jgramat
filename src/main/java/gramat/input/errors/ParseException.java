package gramat.input.errors;

import gramat.input.Location;

public class ParseException extends RuntimeException {
    private final Location location;

    public ParseException(String message, Location location) {
        super(generate_message(message, location));
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    private static String generate_message(String message, Location location) {
        return String.format("%s (Ln. %s, Col. %s; %s)", message, location.line, location.column, location.source);
    }
}
