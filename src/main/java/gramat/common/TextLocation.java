package gramat.common;

public class TextLocation {

    public final String source;
    public final int position;
    public final int line;
    public final int column;

    public TextLocation(String source, int position, int line, int column) {
        this.source = source;
        this.position = position;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }

}
