package gramat.symbols;

import java.util.List;

public class SymbolFactory {

    public Symbol literal(char c) {
        return new SymbolLiteral(c);
    }

    public Symbol range(char start, char end) {
        return new SymbolRange(start, end);
    }

    public Symbol not(Symbol symbol) {
        return new SymbolNot(symbol);
    }

    public Symbol union(Symbol... symbol) {
        return new SymbolUnion(List.of(symbol));
    }

}
