package gramat.engine.symbols;

import gramat.engine.checks.Check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SymbolSource implements Iterable<Symbol> {

    private final List<Symbol> symbols;

    public SymbolSource() {
        symbols = new ArrayList<>();
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

    public SymbolCheck getCheck(Symbol symbol, Check check) {
        for (var s : symbols) {
            if (s instanceof SymbolCheck) {
                var sch = (SymbolCheck)s;

                if (Objects.equals(sch.symbol, symbol) && Objects.equals(sch.check, check)) {
                    return sch;
                }
            }
        }

        var sch = new SymbolCheck(symbol, check);

        symbols.add(sch);

        return sch;
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
}
