package org.gramat;

public class GrammarException extends RuntimeException {

    private final Location location;
    private final LocationRange range;

    public GrammarException(String message, Location location) {
        this(message, location, location.range(null), null);
    }

    public GrammarException(String message, LocationRange range) {
        this(message, range.getBeginLocation(), range, null);
    }

    public GrammarException(String message, Location location, Throwable cause) {
        this(message, location, location.range(null), cause);
    }

    public GrammarException(String message, LocationRange range, Throwable cause) {
        this(message, range.getBeginLocation(), range, cause);
    }

    private GrammarException(String message, Location location, LocationRange range, Throwable cause) {
        super(message, cause);
        this.location = location;
        this.range = range;
    }

    public Location getLocation() {
        return location;
    }

    public LocationRange getRange() {
        return range;
    }

    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return ((message != null) ? (s + ": " + message) : s) + " (" + range + ")";
    }

}
