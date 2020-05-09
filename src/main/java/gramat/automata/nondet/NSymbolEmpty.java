package gramat.automata.nondet;

public class NSymbolEmpty extends NSymbol {
    @Override
    public boolean matches(NSymbol symbol) {
        return symbol instanceof NSymbolEmpty;
    }

    @Override
    public String toString() {
        return "ε";
    }
}
