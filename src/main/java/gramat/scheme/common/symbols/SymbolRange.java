package gramat.scheme.common.symbols;

import gramat.exceptions.UnsupportedValueException;
import gramat.util.PP;

public class SymbolRange implements Symbol {

    public final char begin;
    public final char end;

    public SymbolRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean test(char c) {
        return c >= begin && c <= end;
    }

    @Override
    public boolean intersects(Symbol other) {
        if (other instanceof SymbolChar) {
            var o = (SymbolChar)other;

            return o.value >= this.begin && o.value <= end;
        }
        else if (other instanceof SymbolRange) {
            var o = (SymbolRange)other;

            return this.begin <= o.end && this.end >= o.begin;
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
        return "range " + PP.str(begin) + "," + PP.str(end);
    }
}
