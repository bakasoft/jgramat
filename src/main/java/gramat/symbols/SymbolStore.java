package gramat.symbols;

import gramat.util.Store;

import java.util.Objects;

public class SymbolStore extends Store<Symbol> {

    public Symbol makeChar(char c) {
        var s = searchChar(c);

        if (s == null) {
            s = new SymbolChar(c);

            items.add(s);
        }

        return s;
    }

    public Symbol makeRange(char begin, char end) {
        var s = searchRange(begin, end);

        if (s == null) {
            s = new SymbolRange(begin, end);

            items.add(s);
        }

        return s;
    }

    public Symbol makeWild() {
        var s = search(i -> i instanceof SymbolWild);

        if (s == null) {
            s = new SymbolWild();

            items.add(s);
        }

        return s;
    }

    public Symbol searchChar(char c) {
        return search(symbol -> {
            if (symbol instanceof SymbolChar) {
                var sch = (SymbolChar)symbol;

                return sch.value == c;
            }
            return false;
        });
    }

    public Symbol searchRange(char begin, char end) {
        return search(symbol -> {
            if (symbol instanceof SymbolRange) {
                var srg = (SymbolRange)symbol;

                return srg.begin == begin && srg.end == end;
            }
            return false;
        });
    }

}
