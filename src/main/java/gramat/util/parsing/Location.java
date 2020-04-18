package gramat.util.parsing;

public class Location {

    private final Source source;
    private final int position;

    private Coordinates coordinates;

    public Location(Source source, int position) {
        this.source = source;
        this.position = position;
    }

    public Source getSource() {
        return source;
    }

    public Coordinates getCoordinates() {
        if (coordinates == null) {
            coordinates = source.coordinatesOf(position);
        }
        return coordinates;
    }

    @Override
    public String toString() {
        return source + "@" + getCoordinates();
    }
}
