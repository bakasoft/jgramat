package gramat.automata.ndfa;

import gramat.util.GramatWriter;

import java.io.IOException;

public class SymbolChar extends Symbol {

    public final char value;

    public SymbolChar(char value) {
        this.value = value;
    }

    @Override
    public void write(Appendable output) throws IOException {
        output.append("[");
        output.append(GramatWriter.toDelimitedString(String.valueOf(value), '\0'));
        output.append("]");
    }
}
