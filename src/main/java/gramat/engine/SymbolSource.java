package gramat.engine;

import java.util.ArrayList;
import java.util.List;

public class SymbolSource {

    private final List<Symbol> symbols;

    private SymbolWild wild;
    private SymbolEmpty empty;

    public SymbolSource() {
        symbols = new ArrayList<>();
    }

    public SymbolChar getChar(char value) {
        for (var symbol : symbols) {
            if (symbol instanceof SymbolChar) {
                var chr = (SymbolChar)symbol;

                if (chr.value == value) {
                    return chr;
                }
            }
        }

        var chr = new SymbolChar(value);

        symbols.add(chr);

        return chr;
    }

    public SymbolRange getRange(char begin, char end) {
        for (var symbol : symbols) {
            if (symbol instanceof SymbolRange) {
                var range = (SymbolRange)symbol;

                if (range.begin == begin && range.end == end) {
                    return range;
                }
            }
        }

        var range = new SymbolRange(begin, end);

        symbols.add(range);

        return range;
    }

    public SymbolWild getWild() {
        if (wild == null) {
            wild = new SymbolWild();
        }
        return wild;
    }

    public SymbolEmpty getEmpty() {
        if (empty == null) {
            empty = new SymbolEmpty();
        }
        return empty;
    }

}
