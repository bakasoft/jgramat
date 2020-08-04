package gramat.engine.symbols;

import gramat.engine.Runner;

public class SymbolWild extends Symbol {

    @Override
    public boolean matches(Runner runner) {
        return false;
    }

    @Override
    public String toString() {
        return "*";
    }
}
