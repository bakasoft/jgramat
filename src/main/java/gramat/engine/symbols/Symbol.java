package gramat.engine.symbols;

abstract public class Symbol {

    abstract public boolean matches(char chr);

    public boolean isNull() {
        return this instanceof SymbolNull;
    }

    public boolean isWild() {
        return this instanceof SymbolWild;
    }

}
