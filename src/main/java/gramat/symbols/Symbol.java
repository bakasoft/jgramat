package gramat.symbols;

public interface Symbol {

    boolean test(char c);

    boolean intersects(Symbol other);

}
