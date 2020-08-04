package gramat.engine.symbols;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SymbolSource implements Iterable<Symbol> {

    private final List<Symbol> symbols;

    public SymbolSource() {
        symbols = new ArrayList<>();
    }

    public SymbolSource(SymbolSource source) {
        this.symbols = new ArrayList<>(source.symbols);
    }

    @Override
    public Iterator<Symbol> iterator() {
        return symbols.iterator();
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
        for (var symbol : symbols) {
            if (symbol instanceof SymbolWild) {
                return (SymbolWild)symbol;
            }
        }

        var wild = new SymbolWild();

        symbols.add(wild);

        return wild;
    }

    public SymbolSource copy() {
        return new SymbolSource(this);
    }
}
