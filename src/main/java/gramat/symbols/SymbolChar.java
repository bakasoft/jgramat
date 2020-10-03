package gramat.symbols;

import gramat.util.PP;

public class SymbolChar extends Symbol {

    public final char value;

    public SymbolChar(char value) {
        this.value = value;
    }

    @Override
    public boolean test(char c) {
        return this.value == c;
    }

    @Override
    public String toString() {
        return "char " + PP.str(value);
    }
}
