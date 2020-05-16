package gramat.automata.ndfa;

import gramat.util.GramatWriter;

import java.io.IOException;

public class SymbolRange extends Symbol {

    public final int begin;
    public final int end;

    public SymbolRange(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void write(Appendable output) throws IOException {
        output.append(toString());
    }

    @Override
    public String toString() {
        return
                "[" + GramatWriter.toDelimitedString(String.valueOf((char)begin), '\0') +
                        "-" + GramatWriter.toDelimitedString(String.valueOf((char)end), '\0') +
                        "]";
    }
}
