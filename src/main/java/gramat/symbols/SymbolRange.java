package gramat.symbols;

import gramat.util.PP;

public class SymbolRange extends Symbol {

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
    public String toString() {
        return "range " + PP.str(begin) + "," + PP.str(end);
    }
}
