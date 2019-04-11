package org.bakasoft.gramat;

import java.util.Objects;

public class Location {

    private final Tape tape;
    private final int position;
    private final int line;
    private final int column;

    public Location(Tape tape, int position, int line, int column) {
        this.tape = tape;
        this.position = position;
        this.line = line;
        this.column = column;
    }

    public int getPosition() { return position; }

    public int getLine() { return line; }

    public int getColumn() { return column; }

    public Tape getTape() { return tape; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location that = (Location)obj;

            return Objects.equals(this.tape, that.tape)
                    && this.position == that.position
                    && this.line == that.line
                    && this.column == that.column;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tape, position, line, column);
    }

    @Override
    public String toString() {
        return tape + " @" + line + ":" + column;
    }

    public LocationRange range(Location end) {
        return new LocationRange(tape, this, end);
    }

    public LocationRange range() {
        return range(tape.getLocation());
    }
}
