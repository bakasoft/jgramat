package gramat.automata.nondet;

import gramat.util.GramatWriter;

public class NSymbolNotChar extends NSymbol {

    public final char value;

    public NSymbolNotChar(char value) {
        this.value = value;
    }

    @Override
    public boolean matches(NSymbol symbol) {
        return symbol instanceof NSymbolNotChar && ((NSymbolNotChar)symbol).value == value;
    }

    @Override
    public String toString() {
        return "[^" + GramatWriter.toDelimitedString(String.valueOf(value), '\0') + "]";
    }
}
