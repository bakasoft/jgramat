package gramat.automata.ndfa;

import gramat.util.GramatWriter;

import java.io.IOException;

public class SymbolRange extends Symbol {

    public final char begin;
    public final char end;

    public SymbolRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void write(Appendable output) throws IOException {
        output.append("[");
        output.append(GramatWriter.toDelimitedString(String.valueOf(begin), '\0'));
        output.append("-");
        output.append(GramatWriter.toDelimitedString(String.valueOf(end), '\0'));
        output.append("]");
    }
}
