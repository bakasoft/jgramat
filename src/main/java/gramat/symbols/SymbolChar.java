package gramat.symbols;

import gramat.exceptions.UnsupportedValueException;
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
    public boolean intersects(Symbol other) {
        if (other instanceof SymbolChar) {
            var o = (SymbolChar)other;

            return value == o.value;
        }
        else if (other instanceof SymbolRange) {
            var o = (SymbolRange)other;

            return value >= o.begin && value <= o.end;
        }
        else if (other instanceof SymbolWild) {
            return false;
        }
        else {
            throw new UnsupportedValueException(other);
        }
    }

    @Override
    public String toString() {
        return "char " + PP.str(value);
    }
}
