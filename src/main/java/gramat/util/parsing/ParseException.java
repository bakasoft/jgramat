package gramat.util.parsing;

public class ParseException extends RuntimeException {

    private final Location location;

    public ParseException(String message, Source source, int position) {
        super(message);
        this.location = new Location(source, position);
    }

    public ParseException(String message, Location location) {
        super(message);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return getMessage() + " <- " + location;
    }
}
