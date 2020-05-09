package gramat.automata.nondet;

import gramat.util.GramatWriter;

public class NSymbolChar extends NSymbol {

    public final char value;

    public NSymbolChar(char value) {
        this.value = value;
    }

    @Override
    public boolean matches(NSymbol symbol) {
        if (symbol instanceof NSymbolChar) {
            var sc = (NSymbolChar) symbol;

            return sc.value == value;
        }
        else if (symbol instanceof NSymbolRange) {
            var sr = (NSymbolRange) symbol;

            return sr.begin == value && sr.end == value;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + GramatWriter.toDelimitedString(String.valueOf(value), '\0') + "]";
    }
}
