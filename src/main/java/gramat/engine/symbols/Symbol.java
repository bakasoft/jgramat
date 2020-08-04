package gramat.engine.symbols;

abstract public class Symbol {

    abstract public boolean matches(char chr);

    public boolean isWild() {
        return this instanceof SymbolWild;
    }

}
