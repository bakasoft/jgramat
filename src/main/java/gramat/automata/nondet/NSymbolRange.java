package gramat.automata.nondet;

import gramat.util.GramatWriter;

public class NSymbolRange extends NSymbol {

    public final char begin;
    public final char end;

    public NSymbolRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean matches(NSymbol symbol) {
        if (symbol instanceof NSymbolChar) {
            var sc = (NSymbolChar) symbol;

            return sc.value == begin && sc.value == end;
        }
        else if (symbol instanceof NSymbolRange) {
            var sr = (NSymbolRange) symbol;

            return sr.begin == begin && sr.end == end;
        }
        return false;
    }

    @Override
    public String toString() {
        var beginStr = GramatWriter.toDelimitedString(String.valueOf(begin), '`');
        var endStr = GramatWriter.toDelimitedString(String.valueOf(end), '`');
        return "[" + beginStr + "-" + endStr + "]";
    }

}
