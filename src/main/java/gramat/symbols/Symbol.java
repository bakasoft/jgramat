package gramat.symbols;

public abstract class Symbol {

    public abstract boolean test(char c);

    public abstract boolean intersects(Symbol other);

}
