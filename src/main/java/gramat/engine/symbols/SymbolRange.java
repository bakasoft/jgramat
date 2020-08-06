package gramat.engine.symbols;

import gramat.engine.AmCode;

public class SymbolRange extends Symbol {

    public final char begin;
    public final char end;

    public SymbolRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        var beginStr = String.valueOf(begin);
        var endStr = String.valueOf(end);
        return "[" + AmCode.escape(beginStr) + "," + AmCode.escape(endStr) + "]";
    }
}
