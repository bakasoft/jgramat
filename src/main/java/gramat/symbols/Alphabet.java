package gramat.symbols;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Alphabet implements Iterable<Symbol> {

    private final ArrayList<Symbol> symbols;

    public Alphabet() {
        symbols = new ArrayList<>();
    }

    public Symbol character(char c) {
        for (var s : symbols) {
            if (s instanceof SymbolChar) {
                var sc = (SymbolChar)s;

                if (sc.value == c) {
                    return s;
                }
            }
        }

        var s = new SymbolChar(c);
        symbols.add(s);
        return s;
    }

    public Symbol range(char begin, char end) {
        for (var s : symbols) {
            if (s instanceof SymbolRange) {
                var sr = (SymbolRange)s;

                if (sr.begin == begin && sr.end == end) {
                    return s;
                }
            }
        }

        var s = new SymbolRange(begin, end);
        symbols.add(s);
        return s;
    }

    public Symbol wild() {
        for (var s : symbols) {
            if (s instanceof SymbolWild) {
                return s;
            }
        }
        var s = new SymbolWild();
        symbols.add(s);
        return s;
    }

    public Symbol reference(String reference) {
        for (var s : symbols) {
            if (s instanceof SymbolReference) {
                var ref = (SymbolReference)s;

                if (Objects.equals(ref.reference, reference)) {
                    return s;
                }
            }
        }

        var s = new SymbolReference(reference);
        symbols.add(s);
        return s;
    }

    @Override
    public Iterator<Symbol> iterator() {
        // TODO check for concurrent modification
        return new ArrayList<>(symbols).iterator();
    }
}
