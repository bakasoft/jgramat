package gramat.scheme.core.symbols;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SymbolMatcher<T> {

    private final Map<Symbol, T> map;

    public SymbolMatcher() {
        map = new LinkedHashMap<>();
    }

    public void add(Symbol symbol, T value) {
        // Check if already defined
        if (map.containsKey(symbol)) {
            throw new RuntimeException();
        }

        // TODO this validation must be moved to the states (not here)
//        // Check if intersects other symbol
//        for (var s : map.keySet()) {
//            if (s.intersects(symbol)) {
//                throw new RuntimeException(s + " intersets " + symbol);
//            }
//        }

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
