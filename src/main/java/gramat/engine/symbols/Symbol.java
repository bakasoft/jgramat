package gramat.engine.symbols;

import gramat.engine.Runner;

abstract public class Symbol {

    abstract public boolean matches(Runner runner);

    public boolean isWild() {
        return this instanceof SymbolWild;
    }

}
