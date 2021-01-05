package gramat.scheme.common.symbols;

public interface Symbol {

    boolean test(char c);

    boolean intersects(Symbol other);

}
