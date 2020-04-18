package gramat.util.parsing;

public class Coordinates {
    private final int line;
    private final int column;

    public Coordinates(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }
}
