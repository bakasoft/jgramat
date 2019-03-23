package org.bakasoft.gramat;

import java.util.Objects;

public class Location {

    private final String name;
    private final int position;
    private final int line;
    private final int column;

    public Location(String name, int position, int line, int column) {
        this.name = name;
        this.position = position;
        this.line = line;
        this.column = column;
    }

    public int getPosition() { return position; }

    public int getLine() { return line; }

    public int getColumn() { return column; }

    public String getName() { return name; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location that = (Location)obj;

            return Objects.equals(this.name, that.name)
                    && this.position == that.position
                    && this.line == that.line
                    && this.column == that.column;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, line, column);
    }

    @Override
    public String toString() {
        return name + " (line " + line + ", column " + column + ")";
    }
}
