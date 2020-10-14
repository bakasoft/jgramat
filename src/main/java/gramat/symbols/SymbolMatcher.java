package gramat.symbols;

import gramat.symbols.Symbol;
import gramat.symbols.SymbolWild;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SymbolMatcher<T> {

    private final Map<Symbol, T> map;

    public SymbolMatcher() {
        map = new HashMap<>();
    }

    public void add(Symbol symbol, T value) {
        // Check if already defined
        if (map.containsKey(symbol)) {
            throw new RuntimeException();
        }

        // Check if intersects other symbol
        for (var s : map.keySet()) {
            if (s.intersects(symbol)) {
                throw new RuntimeException();
            }
        }

        map.put(symbol, value);
    }

    public T match(char chr) {
        T fallback = null;

        for (var entry : map.entrySet()) {
            var symbol = entry.getKey();
            var value = entry.getValue();

            if (symbol instanceof SymbolWild) {
                fallback = value;
            }
            else if (symbol.test(chr)) {
                return value;
            }
        }

        return fallback;
    }

    public Set<Symbol> getSymbols() {
        return map.keySet();
    }

    public T get(Symbol symbol) {
        return map.get(symbol);
    }
}