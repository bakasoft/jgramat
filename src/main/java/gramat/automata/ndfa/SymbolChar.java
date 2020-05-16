package gramat.automata.ndfa;

import gramat.util.GramatWriter;
import gramat.util.parsing.Source;

import java.io.IOException;

public class SymbolChar extends Symbol {

    public final int value;

    public SymbolChar(int value) {
        this.value = value;
    }

    @Override
    public void write(Appendable output) throws IOException {
        output.append(toString());
    }

    @Override
    public String toString() {
        if (value == Source.EOF) {
            return "$";
        }
        return GramatWriter.toDelimitedString(String.valueOf((char)value), '\"');
    }
}
