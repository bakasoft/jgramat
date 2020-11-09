package gramat.scheme.core.symbols;

public interface Symbol {

    boolean test(char c);

    boolean intersects(Symbol other);

}
