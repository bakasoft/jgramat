package gramat.automata.nondet;

public class NSymbolWild extends NSymbol {

    @Override
    public boolean matches(NSymbol symbol) {
        return false;
    }

    @Override
    public String toString() {
        return "*";
    }
}
