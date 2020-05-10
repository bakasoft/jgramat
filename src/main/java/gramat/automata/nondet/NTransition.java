package gramat.automata.nondet;

public class NTransition {

    NState source;
    NSymbol symbol;
    NState target;

    public NTransition(NState source, NSymbol symbol, NState target) {
        this.source = source;
        this.symbol = symbol;
        this.target = target;
    }

    @Override
    public String toString() {
        return source + " -> " + target + ": " + symbol;
    }

    public boolean isChar() {
        return symbol instanceof NSymbolChar || symbol instanceof NSymbolRange;
    }
}
